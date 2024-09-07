package com.recipes.controller;

import com.recipes.dto.*;
import com.recipes.result.PageResult;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
import com.recipes.utils.UserHolder;
import com.recipes.vo.RecipeSimpleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = {"http://localhost:8082", "http://localhost:3000"})
@Slf4j
@Tag(name = "Recipe API", description = "Operations related to recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe details", description = "Get the details of a specific recipe")
    public Result<RecipeDTO> getRecipeDetails(@PathVariable("id") Long id) {
        // 构造redis中的key，规则：recipe_ + id
        String key = "recipe_" + id;

        // 查询redis中是否存在菜品数据
        RecipeDTO recipeDTO = (RecipeDTO) redisTemplate.opsForValue().get(key);
        if (recipeDTO != null) {
            // 如果存在，直接返回，无须查询数据库
            return Result.success(recipeDTO);
        }

        // 如果不存在，查询数据库
        log.info("Getting details for recipe with id={}", id);
        Result<RecipeDTO> result = recipeService.getRecipeDetails(id);

        // 将查询到的数据放入redis中
        if (result != null && result.getCode() != null && result.getCode() == 1) {
            redisTemplate.opsForValue().set(key, result.getData());
        }

        // 返回查询结果
        return result;
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular recipes", description = "Get a list of popular recipes based on likes and comments")
    public Result<PageResult> getPopularRecipes() {
        log.info("Getting popular recipes");
        return recipeService.getPopularRecipes();
    }


    @GetMapping("/tag/popular")
    @Operation(summary = "Get popular recipes by tag with pagination", description = "Get a list of popular recipes based on likes and comments for a specific tag with pagination")
    public Result<PageResult<RecipeSimpleVO>> getPopularRecipesByTag(
            @Parameter(description = "Tag name", required = true) @RequestParam String tag,
            @Parameter(description = "Page number", required = true) @RequestParam int page,
            @Parameter(description = "Number of records per page", required = true) @RequestParam int pageSize) {
        log.info("Request received for tag: {}, page: {}, pageSize: {}", tag, page, pageSize);  // 添加日志
        return recipeService.getPopularRecipesByTag(tag, page, pageSize);
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes", description = "Search for recipes based on keyword, tags, and other criteria")
    public Result<PageResult> searchRecipes(
            @Parameter(description = "Search keyword", in = ParameterIn.QUERY) @RequestParam(required = false) String keyword,
            @Parameter(description = "Tag name", in = ParameterIn.QUERY) @RequestParam(required = false) String tagName,
            @Parameter(description = "Sort by", in = ParameterIn.QUERY) @RequestParam(required = false) String sortBy,
            @Parameter(description = "Page number", in = ParameterIn.QUERY) @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of records per page", in = ParameterIn.QUERY) @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Searching recipes with keyword={}, tagName={}, sortBy={}, page={}, pageSize={}", keyword, tagName, sortBy, page, pageSize);
        RecipePageQueryDTO queryDTO = new RecipePageQueryDTO(page, pageSize, keyword);
        return recipeService.searchRecipes(queryDTO, sortBy, tagName);
    }

    @PostMapping("/publish")
    @Operation(summary = "Publish a recipe", description = "Publishes a recipe to the user's recipe list")
    public Result<RecipeDTO> publishRecipe(@RequestBody RecipeDTO recipeDTO) {
        Long userId = UserHolder.getUser().getId();
        log.info("Publishing recipe: userId={}, recipeDTO={}", userId, recipeDTO);
        return recipeService.publishRecipe(recipeDTO);
    }

    @GetMapping("/getAllMyRecipes")
    @Operation(summary = "Get all my recipes", description = "Get all recipes published by the user")
    public Result<List<RecipeDTO>> getUserRecipes() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting recipes for user with id={}", userId);
        return recipeService.getUserRecipes(userId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a recipe", description = "Deletes a published recipe")
    public Result<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe to be deleted", required = true, in = ParameterIn.QUERY)
            @RequestParam Long recipeId) {
        Long userId = UserHolder.getUser().getId();
        log.info("Deleting recipe: userId={}, recipeId={}", userId, recipeId);

        //将所有的缓存数据清理掉
        String key = "recipe_" + recipeId;
        cleanCache(key);

        return recipeService.deleteRecipe(userId, recipeId);
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get recipe details for editing", description = "Get the details of a specific recipe for editing")
    public Result<RecipeDTO> getRecipeDetailsForEdit(@PathVariable Long id) {
        log.info("Getting details for recipe with id={}", id);
        return recipeService.getRecipeDetails(id);
    }

    @PostMapping("/edit")
    @Operation(summary = "Edit a recipe", description = "Edit an existing recipe")
    public ResponseEntity<Result<RecipeDTO>> editRecipe(@RequestBody RecipeDTO recipeDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        log.info("Editing recipe: userId={}, recipeDTO={}", userId, recipeDTO);

        try {
            // 验证当前用户是否为食谱的发布者
            if (!recipeService.isRecipeOwner(userId, recipeDTO.getId())) {
                log.error("User {} is not the owner of recipe {}", userId, recipeDTO.getId());
                return new ResponseEntity<>(Result.error("You do not have permission to edit this recipe"), HttpStatus.FORBIDDEN);
            }

            Result<RecipeDTO> result = recipeService.editRecipe(userId, recipeDTO);
            // 删除Redis缓存
            String key = "recipe_" + recipeDTO.getId();
            cleanCache(key);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error editing recipe", e);
            return new ResponseEntity<>(Result.error("Error editing recipe"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all recipes", description = "Get a list of all recipes with pagination")
    public Result<PageResult> getAllRecipes(
            @Parameter(description = "Page number", in = ParameterIn.QUERY) @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of records per page", in = ParameterIn.QUERY) @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all recipes with pagination");
        RecipePageQueryDTO queryDTO = new RecipePageQueryDTO(page, size, null);
        return recipeService.getAllRecipes(queryDTO);
    }
}
