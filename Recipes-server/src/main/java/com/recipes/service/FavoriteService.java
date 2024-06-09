package com.recipes.service;

import com.recipes.dto.FavoriteDTO;
import com.recipes.result.Result;

public interface FavoriteService {
    Result<FavoriteDTO> addToFavorites(Long recipeId);
    Result<Void> removeFromFavorites(Long recipeId);
}
