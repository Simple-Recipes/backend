package com.recipes.controller;

import com.recipes.dto.*;
import com.recipes.result.PageResult;
import com.recipes.result.Result;
import com.recipes.service.CommentService;
import com.recipes.service.LikeService;
import com.recipes.service.RecipeService;
import com.recipes.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Recipe API", description = "Operations related to recipes")
public class RecipeController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private HttpSession session;

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe details", description = "Get the details of a specific recipe")
    public Result<RecipeDTO> getRecipeDetails(@PathVariable Long id) {
        log.info("Getting details for recipe with id={}", id);
        return recipeService.getRecipeDetails(id);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular recipes", description = "Get a list of popular recipes based on likes and comments")
    public Result<PageResult> getPopularRecipes() {
        log.info("Getting popular recipes");
        return recipeService.getPopularRecipes();
    }

    @GetMapping("/search")
    @Operation(summary = "Search recipes", description = "Search for recipes based on keyword, tags, and other criteria")
    public Result<PageResult> searchRecipes(
            @Parameter(description = "Search keyword", in = ParameterIn.QUERY) @RequestParam(required = false) String keyword,
            @Parameter(description = "Tag name", in = ParameterIn.QUERY) @RequestParam(required = false) String tagName,
            @Parameter(description = "Sort by", in = ParameterIn.QUERY) @RequestParam(required = false) String sortBy,
            @Parameter(description = "Page number", in = ParameterIn.QUERY) @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of records per page", in = ParameterIn.QUERY) @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Searching recipes with keyword={}, tagName={}, sortBy={}, page={}, pageSize={}", keyword, tagName, sortBy, page, pageSize);
        RecipePageQueryDTO queryDTO = new RecipePageQueryDTO(page, pageSize, keyword);
        return recipeService.searchRecipes(queryDTO, sortBy, tagName);
    }

    @PostMapping("/publish")
    @Operation(summary = "Publish a recipe", description = "Publishes a recipe to the user's recipe list")
    public Result<RecipeDTO> publishRecipe(@RequestBody RecipeDTO recipeDTO) {
        Long userId = (Long) session.getAttribute("userId");
        log.info("Publishing recipe: userId={}, recipeDTO={}", userId, recipeDTO);
        return recipeService.publishRecipe(recipeDTO);
    }

    @GetMapping("/user-recipes")
    @Operation(summary = "Get user's recipes", description = "Get all recipes published by the user")
    public Result<List<RecipeDTO>> getUserRecipes() {
        Long userId = (Long) session.getAttribute("userId");
        log.info("Getting recipes for user with id={}", userId);
        return recipeService.getUserRecipes(userId);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a recipe", description = "Deletes a published recipe")
    public Result<Void> deleteRecipe(@RequestParam Long recipeId) {
        Long userId = (Long) session.getAttribute("userId");
        log.info("Deleting recipe: userId={}, recipeId={}", userId, recipeId);
        return recipeService.deleteRecipe(userId, recipeId);
    }


    //
    @PostMapping("/comment")
    @Operation(summary = "Comment on a recipe", description = "Submit a comment on a recipe")
    public Result<CommentDTO> commentOnRecipe(@RequestBody CommentDTO commentDTO) {
        log.info("Submitting comment: {}", commentDTO);
        return commentService.commentOnRecipe(commentDTO);
    }

    @GetMapping("/comments")
    @Operation(summary = "Get recipe comments", description = "Get a list of comments for a specific recipe")
    public Result<List<CommentDTO>> getRecipeComments(@RequestParam Long recipeId) {
        log.info("Getting comments for recipe with id={}", recipeId);
        return commentService.getRecipeComments(recipeId);
    }

    @PostMapping("/like")
    @Operation(summary = "Like a recipe", description = "Like a recipe")
    public Result<LikeDTO> likeRecipe(@RequestBody LikeDTO likeDTO) {
        log.info("Liking recipe: {}", likeDTO);
        return likeService.likeRecipe(likeDTO);
    }

    @PostMapping("/unlike")
    @Operation(summary = "Unlike a recipe", description = "Unlike a recipe")
    public Result<LikeDTO> unlikeRecipe(@RequestBody LikeDTO likeDTO) {
        log.info("Unliking recipe: {}", likeDTO);
        return likeService.unlikeRecipe(likeDTO);
    }

    @GetMapping("/likes")
    @Operation(summary = "Get recipe likes", description = "Get the number of likes for a specific recipe")
    public Result<Integer> getRecipeLikes(@RequestParam Long recipeId) {
        log.info("Getting likes for recipe with id={}", recipeId);
        return likeService.getRecipeLikes(recipeId);
    }

    @GetMapping("/tags")
    @Operation(summary = "Get all tags", description = "Get a list of all tags")
    public Result<List<TagDTO>> getAllTags() {
        log.info("Getting all tags");
        return tagService.getAllTags();
    }

    @PostMapping("/tags")
    @Operation(summary = "Add a new tag", description = "Add a new tag")
    public Result<TagDTO> addTag(@RequestBody TagDTO tagDTO) {
        log.info("Adding new tag: {}", tagDTO);
        return tagService.addTag(tagDTO);
    }

    @DeleteMapping("/tags/{id}")
    @Operation(summary = "Delete a tag", description = "Delete a tag")
    public Result<Void> deleteTag(@PathVariable Long id) {
        log.info("Deleting tag with id={}", id);
        return tagService.deleteTag(id);
    }
}
