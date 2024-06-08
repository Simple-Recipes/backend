package com.recipes.service.impl;

import com.recipes.dao.FavoriteDAO;
import com.recipes.dto.FavoriteDTO;
import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import com.recipes.mapper.FavoriteMapper;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDAO favoriteDAO;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Result<FavoriteDTO> addToFavorites(Long userId, Long recipeId) {
        FavoriteId favoriteId = new FavoriteId(userId, recipeId);
        Favorite favorite = Favorite.builder()
                .id(favoriteId)
                .createTime(LocalDateTime.now())
                .build();
        favoriteDAO.saveFavorite(favorite);
        FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);
        return Result.success(favoriteDTO);
    }

    @Override
    public Result<Void> removeFromFavorites(Long userId, Long recipeId) {
        FavoriteId favoriteId = new FavoriteId(userId, recipeId);
        Favorite favorite = favoriteDAO.findFavoriteById(favoriteId);
        if (favorite != null) {
            favoriteDAO.deleteFavorite(favorite.getId());
            return Result.success();
        } else {
            return Result.error("Favorite not found");
        }
    }
}
