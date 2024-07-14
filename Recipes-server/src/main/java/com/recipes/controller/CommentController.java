// CommentController.java
package com.recipes.controller;

import com.recipes.dto.CommentDTO;
import com.recipes.result.Result;
import com.recipes.service.CommentService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Comment API", description = "Operations related to comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/postRecipeComment")
    @Operation(summary = "Post a comment on a recipe", description = "Submit a comment on a recipe")
    public Result<CommentDTO> commentOnRecipe(@RequestBody CommentDTO commentDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.error("User is not logged in");
        }
        commentDTO.setUserId(userId);
        log.info("Submitting comment: {}", commentDTO);
        return commentService.commentOnRecipe(commentDTO);
    }

    @GetMapping("/getRecipeComments")
    @Operation(summary = "Get recipe comments", description = "Get a list of comments for a specific recipe")
    public Result<List<CommentDTO>> getRecipeComments(@RequestParam Long recipeId) {
        log.info("Getting comments for recipe with id={}", recipeId);
        return commentService.getRecipeComments(recipeId);
    }

    @GetMapping("/getAllMyComments")
    @Operation(summary = "Get all my comments", description = "Get all comments made by the user")
    public Result<List<CommentDTO>> getAllMyComments() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting all comments for user with id={}", userId);
        return commentService.getAllMyComments(userId);
    }

    @DeleteMapping("/deleteComment")
    @Operation(summary = "Delete a comment", description = "Deletes a comment made by the user")
    public ResponseEntity<Result<Void>> deleteComment(
            @Parameter(description = "Comment ID", required = true, in = ParameterIn.QUERY)
            @RequestParam Long commentId) {
        log.info("Deleting comment with id={}", commentId);
        Result<Void> result = commentService.deleteComment(commentId);
        HttpStatus status = result.getCode() == 1 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(result, status);
    }
}
