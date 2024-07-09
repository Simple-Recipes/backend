package com.recipes.service;

import com.recipes.dto.LikeDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;

import java.util.List;

public interface LikeService {
    Result<LikeDTO> likeRecipe(Long userId, Long recipeId) ;
    Result<Void> unlikeRecipe(Long userId, Long recipeId);
    Result<List<RecipeDTO>> getAllMyLikes(Long userId);
    Result<Integer> getRecipeLikes(Long recipeId);

}
