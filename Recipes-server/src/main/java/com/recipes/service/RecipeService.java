package com.recipes.service;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.RecipePageQueryDTO;
import com.recipes.entity.Recipe;
import com.recipes.result.PageResult;
import com.recipes.result.Result;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RecipeService {

    Result<RecipeDTO> getRecipeDetails(Long id);

    Result<PageResult> getPopularRecipes();

    Result<PageResult> searchRecipes(RecipePageQueryDTO queryDTO, String sortBy, String tagName);

    Result<RecipeDTO> publishRecipe(RecipeDTO recipeDTO);

    Result<List<RecipeDTO>> getUserRecipes(Long userId);

    boolean isRecipeOwner(Long userId, Long recipeId);

    Result<Void> deleteRecipe(Long userId, Long recipeId);

    Result<RecipeDTO> editRecipe(Long userId, RecipeDTO recipeDTO);

    Result<PageResult> getAllRecipes(RecipePageQueryDTO queryDTO);

    //Result queryRecipeByUserId(Long id);


}
