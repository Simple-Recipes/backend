package com.recipes.service;

import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;

public interface RecipeService {
    Result<RecipeDTO> publishRecipe(RecipeDTO recipeDTO);
    Result<Void> deleteRecipe(Long recipeId);
}
