package com.recipes.service.impl;

import com.recipes.dao.FavoriteDAO;
import com.recipes.dto.FavoriteDTO;
import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.mapper.FavoriteMapper;
import com.recipes.dao.RecipeDAO;
import com.recipes.dao.UserDAO;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDAO favoriteDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HttpSession session;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public Result<FavoriteDTO> addToFavorites(Long recipeId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        User user = userDAO.findUserById(userId);
        Recipe recipe = recipeDAO.findRecipeById(recipeId);
        if (user != null && recipe != null) {
            Favorite favorite = Favorite.builder()
                    .id(new FavoriteId(userId, recipeId))
                    .user(user)
                    .recipe(recipe)
                    .build();
            favoriteDAO.saveFavorite(favorite);

            FavoriteDTO favoriteDTO = favoriteMapper.toDto(favorite);

            return Result.success(favoriteDTO);
        } else {
            return Result.error("User or Recipe not found");
        }
    }

    @Override
    public Result<Void> removeFromFavorites(Long recipeId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        FavoriteId favoriteId = new FavoriteId(userId, recipeId);
        Favorite favorite = favoriteDAO.findFavoriteById(favoriteId);
        if (favorite != null) {
            favoriteDAO.deleteFavorite(favorite);
            return Result.success();
        } else {
            return Result.error("Favorite not found");
        }
    }
}
