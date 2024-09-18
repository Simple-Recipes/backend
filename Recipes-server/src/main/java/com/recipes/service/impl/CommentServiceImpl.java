package com.recipes.service.impl;

import com.recipes.dao.CommentDAO;
import com.recipes.dao.NotificationDAO;
import com.recipes.dao.RecipeDAO;
import com.recipes.dto.CommentDTO;
import com.recipes.entity.Comment;
import com.recipes.entity.Notification;
import com.recipes.handler.NotificationHandler;
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

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Autowired
    private NotificationHandler notificationHandler;


    @Override
    public Result<CommentDTO> commentOnRecipe(CommentDTO commentDTO) {
        // Convert the comment to an entity object and return
        Comment comment = commentMapper.toEntity(commentDTO);

        // Save the comment
        commentDAO.saveComment(comment);

//        // Get the recipient ID
//        Long recipientId = recipeDAO.findUserIdByRecipeId(comment.getRecipe().getId());
//
//        // Get the recipe title
//        String recipeTitle = recipeDAO.findTitleById(comment.getRecipe().getId());
//
//        // Create the recipe notification
//        Notification notification = new Notification();
//        notification.setRecipientId(recipientId);
//        notification.setSenderId(comment.getUser().getId());
//        notification.setRecipeId(comment.getRecipe().getId());
//        notification.setCommentId(comment.getId());
//        notification.setMessage("User" + comment.getUser().getId() + " commented on your recipe: " + recipeTitle);
//
//        // Storage the notification
//        notificationDAO.saveNotification(notification);
//
//        // Send notification
//        notificationHandler.sendNotification(notification.getMessage());

        // Return the notification
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

    @Override
    public Result<List<CommentDTO>> getAllMyComments(Long userId) {
        List<Comment> comments = commentDAO.findCommentsByUserId(userId);
        return Result.success(commentMapper.toDtoList(comments));
    }

    @Override
    public Result<Void> deleteComment(Long commentId) {
        commentDAO.deleteComment(commentId);
        return Result.success();
    }
}

