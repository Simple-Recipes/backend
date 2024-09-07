package com.recipes.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.recipes.aop.CacheClient;
import com.recipes.dao.RecipeDAO;
import com.recipes.dao.UserDAO;
import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipePageQueryDTO;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.PageResult;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
import com.recipes.utils.JsonConversionUtil;
import com.recipes.utils.RedisData;
import com.recipes.utils.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.recipes.utils.RedisConstants.*;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecipeMapper recipeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result<RecipeDTO> getRecipeDetails(Long id) {
        Recipe recipe = null;

        //逻辑过期解决缓存击穿
        // 1. 尝试通过逻辑过期来获取缓存数据
        recipe = cacheClient.queryWithLogicalExpire(CACHE_RECIPE_KEY, id);
        if (recipe != null) {
            // 如果缓存存在且没有过期，直接返回
            return Result.success(recipeMapper.toDto(recipe));
        }
        //解决缓存穿透(请求不存在的数据
        // 2. 如果逻辑过期未命中，使用缓存穿透策略尝试获取数据
        recipe = cacheClient.queryWithPassThrough(CACHE_RECIPE_KEY, id);
        if (recipe != null) {
            // 如果数据在缓存中存在，直接返回
            return Result.success(recipeMapper.toDto(recipe));
        }
        // 3. 如果缓存穿透未命中，使用互斥锁策略来获取数据（防止缓存击穿）
        recipe = cacheClient.queryWithMutex(id);
        if (recipe == null) {
            return Result.error("Recipe does not exist");
        }

        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        return Result.success(recipeDTO);
    }

    private void saveRecipeRedis(Long id, Long expireSeconds) throws InterruptedException {
        Recipe recipe = recipeDAO.findRecipeById(id);
        Thread.sleep(200);
        RedisData redisData = new RedisData();
        redisData.setData(recipe);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        stringRedisTemplate.opsForValue().set(CACHE_RECIPE_KEY + id, JSONUtil.toJsonStr(redisData));

    }

    @Override
    public Result<PageResult> getPopularRecipes() {
        List<Recipe> recipes = recipeDAO.findPopularRecipes();
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
        PageResult pageResult = new PageResult(recipeDTOs.size(), recipeDTOs);
        return Result.success(pageResult);
    }

    @Override
    public Result<PageResult> searchRecipes(RecipePageQueryDTO queryDTO, String sortBy, String tagName) {
        List<Recipe> recipes = recipeDAO.searchRecipes(queryDTO, sortBy, tagName);
        long total = recipeDAO.countSearchRecipes(queryDTO, tagName);
        List<RecipeDTO> recipeDTOs = recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList());
        return Result.success(new PageResult(total, recipeDTOs));
    }

    @Override
    public Result<RecipeDTO> publishRecipe(RecipeDTO recipeDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        User user = userDAO.findUserById(userId);
        if (user == null) {
            return Result.error("User not found");
        }

        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe.setUser(user);
        recipe.setCreateTime(LocalDateTime.now());
        recipe.setUpdateTime(LocalDateTime.now());

        recipeDAO.saveRecipe(recipe);

        RecipeDTO savedRecipeDTO = recipeMapper.toDto(recipe);

        return Result.success(savedRecipeDTO);
    }

    @Override
    public Result<List<RecipeDTO>> getUserRecipes(Long userId) {
        log.info(String.valueOf(userId));
        List<Recipe> recipes = recipeDAO.findRecipesByUserId(userId);
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(recipeDTOs);
    }

    @Override
    public boolean isRecipeOwner(Long userId, Long recipeId) {
        Recipe recipe = recipeDAO.findRecipeById(recipeId);
        return recipe != null && recipe.getUser().getId().equals(userId);
    }

    @Override
    public Result<Void> deleteRecipe(Long userId, Long recipeId) {
        if (!recipeDAO.existsByIdAndUserId(recipeId, userId)) {
            return Result.error("Recipe not found or you do not have permission to delete this recipe");
        }
        recipeDAO.deleteRecipe(recipeId);
        return Result.success();
    }

    @Override
    @Transactional
    public Result<RecipeDTO> editRecipe(Long userId, RecipeDTO recipeDTO) {
        Recipe recipe = recipeDAO.findRecipeById(recipeDTO.getId());

        if (recipe == null || !recipe.getUser().getId().equals(userId)) {
            return Result.error("Recipe not found or you do not have permission to edit this recipe");
        }

        // Update only the fields that are provided in the DTO
        if (recipeDTO.getTitle() != null) {
            recipe.setTitle(recipeDTO.getTitle());
        }
        if (recipeDTO.getIngredients() != null) {
            recipe.setIngredients(JsonConversionUtil.convertArrayToJson(recipeDTO.getIngredients()));
        }
        if (recipeDTO.getDirections() != null) {
            recipe.setDirections(JsonConversionUtil.convertArrayToJson(recipeDTO.getDirections()));
        }
        if (recipeDTO.getLink() != null) {
            recipe.setLink(recipeDTO.getLink());
        }
        if (recipeDTO.getNutrition() != null) {
            recipe.setNutrition(JsonConversionUtil.convertArrayToJson(recipeDTO.getNutrition()));
        }
        stringRedisTemplate.delete(CACHE_RECIPE_KEY + recipeDTO.getId());

        recipeDAO.saveRecipe(recipe);

        RecipeDTO updatedRecipeDTO = recipeMapper.toDto(recipe);
        return Result.success(updatedRecipeDTO);
    }

    @Override
    public Result<PageResult> getAllRecipes(RecipePageQueryDTO queryDTO) {
        List<Recipe> recipes = recipeDAO.findAllRecipes(queryDTO);
        long total = recipeDAO.countAllRecipes(queryDTO);
        List<RecipeDTO> recipeDTOs = recipes.stream().map(recipeMapper::toDto).collect(Collectors.toList());
        PageResult pageResult = new PageResult(total, recipeDTOs);
        return Result.success(pageResult);
    }

//    @Override
//    public Result queryRecipeByUserId(Long id) {
//        Recipe recipe = recipeDAO.findRecipeById(id);
//        if (recipe == null) {
//            return Result.error("Recipe is not logged in");
//        }
//        Long userId = recipe.getUser().getId();
//        //recipe.setUser;
//
//        User user = userDAO.findUserById(userId);
//        if (user == null) {
//            return Result.error("User not found");
//        }
//        return null;
//    }


}
