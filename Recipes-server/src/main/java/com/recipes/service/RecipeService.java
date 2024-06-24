package com.recipes.service;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipePageQueryDTO;
import com.recipes.result.PageResult;
import com.recipes.result.Result;

import java.util.List;

public interface RecipeService {
    Result<RecipeDTO> getRecipeDetails(Long id);
    Result<PageResult> getPopularRecipes();
    Result<PageResult> searchRecipes(RecipePageQueryDTO queryDTO, String sortBy, String tagName);
    Result<RecipeDTO> publishRecipe(RecipeDTO recipeDTO);
    Result<RecipeDTO> editRecipe(Long userId, RecipeDTO recipeDTO);

    Result<List<RecipeDTO>> getUserRecipes(Long userId);
    Result<List<RecipeDTO>> getAllMyRecipes(Long userId);
    Result<Void> deleteRecipe(Long userId, Long recipeId);
}
