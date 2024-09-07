package com.recipes.controller;

import com.recipes.dto.LikeDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.LikeService;
import com.recipes.utils.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@CrossOrigin(origins = {"http://localhost:8082", "http://localhost:3000"})
@Slf4j
@Tag(name = "Like API", description = "Operations related to likes")
public class LikeController {

    @Autowired
    private LikeService likeService;


    @PostMapping("/likeRecipes")
    @Operation(summary = "Like a recipe", description = "Like a recipe")
    public ResponseEntity<Result<LikeDTO>> likeRecipe(
            @Parameter(description = "LikeDTO containing recipeId", required = true)
            @RequestBody LikeDTO likeDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        Long recipeId = likeDTO.getRecipeId();
        log.info("Liking recipe: userId={}, recipeId={}", userId, recipeId);
        likeDTO.setUserId(userId);
        Result<LikeDTO> result = likeService.likeRecipe(userId, recipeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/UnlikeRecipe")
    @Operation(summary = "Unlike a recipe", description = "Unlike a recipe")
    public ResponseEntity<Result<Void>> unlikeRecipe(
            @Parameter(description = "LikeDTO containing recipeId", required = true)
            @RequestBody LikeDTO likeDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        Long recipeId = likeDTO.getRecipeId();
        log.info("Unliking recipe: userId={}, recipeId={}", userId, recipeId);
        Result<Void> result = likeService.unlikeRecipe(userId, recipeId);
        HttpStatus status = result.getCode() == 1 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/getAllMyLikes")
    @Operation(summary = "Get all likes", description = "Get all recipes liked by the user")
    public Result<List<RecipeDTO>> getAllMyLikes() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting all likes for user with id={}", userId);
        return likeService.getAllMyLikes(userId);
    }

    @GetMapping("/count")
    @Operation(summary = "Get recipe likes count", description = "Get the number of likes for a specific recipe")
    public ResponseEntity<Result<Integer>> getRecipeLikes(@RequestParam Long recipeId) {
        log.info("Getting likes count for recipe with id={}", recipeId);
        Result<Integer> result = likeService.getRecipeLikes(recipeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}