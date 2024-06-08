package com.recipes.service;

import com.recipes.dto.LikeDTO;
import com.recipes.result.Result;

public interface LikeService {
    Result<LikeDTO> likeRecipe(LikeDTO likeDTO);
    Result<LikeDTO> unlikeRecipe(LikeDTO likeDTO);
    Result<Integer> getRecipeLikes(Long recipeId);
}
