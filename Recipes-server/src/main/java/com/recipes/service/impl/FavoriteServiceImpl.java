package com.recipes.service.impl;

import com.recipes.dao.FavoriteDAO;
import com.recipes.dao.RecipeDAO;
import com.recipes.dao.UserDAO;
import com.recipes.dto.FavoriteDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.mapper.FavoriteMapper;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private FavoriteDAO favoriteDAO;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public Result<FavoriteDTO> addToFavorites(FavoriteDTO favoriteDTO) {
        User user = userDAO.findUserById(favoriteDTO.getUserId());
        Recipe recipe = recipeDAO.findRecipeById(favoriteDTO.getRecipeId());

        if (user == null) {
            return Result.error("User not found");
        }

        if (recipe == null) {
            return Result.error("Recipe not found");
        }

        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite.setUser(user);
        favorite.setRecipe(recipe);
        favorite.setId(new FavoriteId(user.getId(), recipe.getId()));

        favoriteDAO.saveFavorite(favorite);

        return Result.success(favoriteMapper.toDto(favorite));
    }

    @Override
    public Result<Void> removeFromFavorites(FavoriteDTO favoriteDTO) {
        FavoriteId favoriteId = new FavoriteId(favoriteDTO.getUserId(), favoriteDTO.getRecipeId());
        favoriteDAO.deleteFavorite(favoriteId);
        return Result.success();
    }
    @Override
    public Result<List<RecipeDTO>> getAllMyFavorites(Long userId) {
        List<Recipe> favoriteRecipes = favoriteDAO.findFavoritesByUserId(userId);
        List<RecipeDTO> favoriteRecipeDTOs = favoriteRecipes.stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
        return Result.success(favoriteRecipeDTOs);
    }
}
