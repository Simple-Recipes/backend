package com.recipes.service.impl;

import com.recipes.dao.CommentDAO;
import com.recipes.dto.CommentDTO;
import com.recipes.entity.Comment;
import com.recipes.result.Result;
import com.recipes.service.CommentService;
import com.recipes.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<CommentDTO> commentDTOs = comments.stream()
                                               .map(commentMapper::toDto)
                                               .collect(Collectors.toList());
        return Result.success(commentDTOs);
    }
}


