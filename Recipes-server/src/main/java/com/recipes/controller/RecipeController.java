package com.recipes.controller;

import com.recipes.context.UserContext;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Api(tags = "Recipe API")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private HttpSession session; // Autowire the HttpSession

    @PostMapping("/publish")
    @ApiOperation(value = "Publish a recipe", notes = "Publishes a recipe to the user's recipe list")
    public Result<RecipeDTO> publishRecipe(
            @ApiParam(value = "Recipe data", required = true)
            @RequestBody RecipeDTO recipeDTO) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        log.info("Publishing recipe: userId={}, recipeDTO={}", userId, recipeDTO);
        return recipeService.publishRecipe(recipeDTO);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Delete a recipe", notes = "Deletes a published recipe")
    public Result<Void> deleteRecipe(
            @ApiParam(value = "Recipe ID", required = true)
            @RequestBody Map<String, Long> request) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        Long recipeId = request.get("recipeId");
        log.info("Deleting recipe: userId={}, recipeId={}", userId, recipeId);
        return recipeService.deleteRecipe(recipeId);
    }
}
