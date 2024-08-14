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
import com.recipes.exception.AlreadyLikedException;
import com.recipes.exception.LikeNotFoundException;
import com.recipes.exception.RecipeNotFoundException;
import com.recipes.mapper.LikeMapper;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.Result;
import com.recipes.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        if (likeDAO.existsById(likeId)) {
            throw new AlreadyLikedException("You have already liked this recipe.");
        }

        Like like = new Like(likeId, user, recipe);
        likeDAO.saveLike(like);

        LikeDTO likeDTO = likeMapper.toDto(like);
        return Result.success(likeDTO);
    }

    @Override
    @Transactional
    public Result<Void> unlikeRecipe(Long userId, Long recipeId) {
        Recipe recipe = recipeDAO.findRecipeById(recipeId);
        if (recipe == null) {
            throw new RecipeNotFoundException("Recipe not found");
        }

        LikeId likeId = new LikeId(userId, recipeId);
        if (!likeDAO.existsById(likeId)) {
            throw new LikeNotFoundException("You have not liked this recipe yet.");
        }

        Like like = likeDAO.findLikeById(likeId);
        likeDAO.deleteLike(likeId);
        return Result.success();
    }


    @Override
    public Result<Integer> getRecipeLikes(Long recipeId) {
        int likes = (int) likeDAO.countLikesByRecipeId(recipeId);
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
