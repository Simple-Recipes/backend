package com.recipes.service;

import com.recipes.dto.FavoriteDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;

import java.util.List;

public interface FavoriteService {
    Result<FavoriteDTO> addToFavorites(FavoriteDTO favoriteDTO);
    Result<Void> removeFromFavorites(FavoriteDTO favoriteDTO);
    Result<List<RecipeDTO>> getAllMyFavorites(Long userId);
}
