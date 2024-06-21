package com.recipes.service.impl;

import com.recipes.dao.LikeDAO;
import com.recipes.dao.RecipeDAO;
import com.recipes.dao.UserDAO;
import com.recipes.dto.LikeDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Like;
import com.recipes.entity.LikeId;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.mapper.LikeMapper;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.Result;
import com.recipes.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDAO likeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public Result<LikeDTO> likeRecipe(Long userId, Long recipeId) {
        User user = userDAO.findUserById(userId);
        Recipe recipe = recipeDAO.findRecipeById(recipeId);

        if (user == null) {
            return Result.error("User not found");
        }

        if (recipe == null) {
            return Result.error("Recipe not found");
        }

        LikeId likeId = new LikeId(userId, recipeId);
        Like like = new Like(likeId, user, recipe);

        likeDAO.saveLike(like);

        LikeDTO likeDTO = likeMapper.toDto(like);
        return Result.success(likeDTO);
    }

    @Override
    public Result<Void> unlikeRecipe(Long userId, Long recipeId) {
        LikeId likeId = new LikeId(userId, recipeId);
        likeDAO.deleteLike(likeId);
        return Result.success();
    }

    @Override
    public Result<Integer> getRecipeLikes(Long recipeId) {
        int likes = likeDAO.countLikesByRecipeId(recipeId);
        return Result.success(likes);
    }

    @Override
    public Result<List<RecipeDTO>> getAllMyLikes(Long userId) {
        List<Like> likedRecipes = likeDAO.findAllByUserId(userId);
        List<RecipeDTO> likedRecipeDTOs = likedRecipes.stream()
                .map(like -> recipeMapper.toDto(like.getRecipe()))
                .collect(Collectors.toList());
        return Result.success(likedRecipeDTOs);
    }
}
