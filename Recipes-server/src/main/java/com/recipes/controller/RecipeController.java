package com.recipes.controller;

import com.recipes.dto.*;
import com.recipes.result.PageResult;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
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
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Recipe API", description = "Operations related to recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

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
        Long userId = UserHolder.getUser().getId();
        log.info("Publishing recipe: userId={}, recipeDTO={}", userId, recipeDTO);
        return recipeService.publishRecipe(recipeDTO);
    }

    @GetMapping("/getAllMyRecipes")
    @Operation(summary = "Get all my recipes", description = "Get all recipes published by the user")
    public Result<List<RecipeDTO>> getUserRecipes() {
        Long userId = UserHolder.getUser().getId();
        log.info("Getting recipes for user with id={}", userId);
        return recipeService.getUserRecipes(userId);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "Delete a recipe", description = "Deletes a published recipe")
    public Result<Void> deleteRecipe(
            @Parameter(description = "ID of the recipe to be deleted", required = true, in = ParameterIn.QUERY)
            @RequestParam Long recipeId) {
        Long userId = UserHolder.getUser().getId();
        log.info("Deleting recipe: userId={}, recipeId={}", userId, recipeId);
        return recipeService.deleteRecipe(userId, recipeId);
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get recipe details for editing", description = "Get the details of a specific recipe for editing")
    public Result<RecipeDTO> getRecipeDetailsForEdit(@PathVariable Long id) {
        log.info("Getting details for recipe with id={}", id);
        return recipeService.getRecipeDetails(id);
    }

    @PostMapping("/edit")
    @Operation(summary = "Edit a recipe", description = "Edit an existing recipe")
    public ResponseEntity<Result<RecipeDTO>> editRecipe(@RequestBody RecipeDTO recipeDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        log.info("Editing recipe: userId={}, recipeDTO={}", userId, recipeDTO);

        try {
            // 验证当前用户是否为食谱的发布者
            if (!recipeService.isRecipeOwner(userId, recipeDTO.getId())) {
                log.error("User {} is not the owner of recipe {}", userId, recipeDTO.getId());
                return new ResponseEntity<>(Result.error("You do not have permission to edit this recipe"), HttpStatus.FORBIDDEN);
            }

            Result<RecipeDTO> result = recipeService.editRecipe(userId, recipeDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error editing recipe", e);
            return new ResponseEntity<>(Result.error("Error editing recipe"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
