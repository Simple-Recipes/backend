package com.recipes.service.impl;

import com.recipes.dao.RecipeDAO;
import com.recipes.dao.UserDAO;
import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Recipe;
import com.recipes.entity.User;
import com.recipes.mapper.RecipeMapper;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private UserDAO userDAO; // 使用UserDAO代替UserRepository

    @Autowired
    private HttpSession session;

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public Result<RecipeDTO> publishRecipe(RecipeDTO recipeDTO) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("User is not logged in");
        }

        User user = userDAO.findUserById(userId);
        if (user == null) {
            return Result.error("User not found");
        }

        Recipe recipe = recipeMapper.toEntity(recipeDTO);
        recipe.setUser(user);
        recipe.setCreateTime(LocalDateTime.now());
        recipe.setUpdateTime(LocalDateTime.now());

        recipeDAO.saveRecipe(recipe);

        RecipeDTO savedRecipeDTO = recipeMapper.toDto(recipe);

        return Result.success(savedRecipeDTO);
    }

    @Override
    public Result<Void> deleteRecipe(Long recipeId) {
        if (recipeDAO.existsById(recipeId)) {
            recipeDAO.deleteRecipe(recipeId);
            return Result.success();
        } else {
            return Result.error("Recipe not found");
        }
    }
}
