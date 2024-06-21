package com.recipes.service.impl;

import com.recipes.dao.CommentDAO;
import com.recipes.dto.CommentDTO;
import com.recipes.entity.Comment;
import com.recipes.mapper.CommentMapper;
import com.recipes.result.Result;
import com.recipes.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Result<CommentDTO> commentOnRecipe(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        commentDAO.saveComment(comment);
        return Result.success(commentMapper.toDto(comment));
    }

    @Override
    public Result<List<CommentDTO>> getRecipeComments(Long recipeId) {
        List<Comment> comments = commentDAO.findCommentsByRecipeId(recipeId);
        List<CommentDTO> commentDTOs = commentMapper.toDtoList(comments);
        return Result.success(commentDTOs);
    }

    @Override
    public Result<List<CommentDTO>> getAllMyComments(Long userId) {
        List<Comment> comments = commentDAO.findCommentsByUserId(userId);
        List<CommentDTO> commentDTOs = commentMapper.toDtoList(comments);
        return Result.success(commentDTOs);
    }

    @Override
    public Result<Void> deleteComment(Long commentId) {
        commentDAO.deleteComment(commentId);
        return Result.success();
    }
}
