package com.recipes.service;

import com.recipes.dto.CommentDTO;
import com.recipes.dto.LikeDTO;
import com.recipes.dto.TagDTO;
import com.recipes.result.Result;

import java.util.List;

public interface CommentService {
    Result<CommentDTO> commentOnRecipe(CommentDTO commentDTO);
    Result<List<CommentDTO>> getRecipeComments(Long recipeId);
}




