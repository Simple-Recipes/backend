package com.recipes.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HttpSession session;

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public Result<RecipeDTO> getRecipeDetails(Long id) {
        Recipe recipe = recipeDAO.findRecipeById(id);
        if (recipe == null) {
            return Result.error("Recipe not found");
        }
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        return Result.success(recipeDTO);
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
        Long userId = (Long) session.getAttribute("userId");
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
        List<Recipe> recipes = recipeDAO.findRecipesByUserId(userId);
        List<RecipeDTO> recipeDTOs = recipes.stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(recipeDTOs);
    }

    @Override
    public Result<Void> deleteRecipe(Long userId, Long recipeId) {
        if (recipeDAO.existsByIdAndUserId(recipeId, userId)) {
            recipeDAO.deleteRecipe(recipeId);
            return Result.success();
        } else {
            return Result.error("Recipe not found or you do not have permission to delete this recipe");
        }
    }
}
