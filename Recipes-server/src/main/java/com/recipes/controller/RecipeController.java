package com.recipes.controller;

import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Recipe API", description = "Operations related to recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private HttpSession session;

    @PostMapping("/publish")
    @Operation(summary = "Publish a recipe", description = "Publishes a recipe to the user's recipe list")
    public Result<RecipeDTO> publishRecipe(@RequestBody RecipeDTO recipeDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Publishing recipe: userId={}, recipeDTO={}", userId, recipeDTO);
        return recipeService.publishRecipe(recipeDTO);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a recipe", description = "Deletes a published recipe")
    public Result<Void> deleteRecipe(@RequestParam Long recipeId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Deleting recipe: userId={}, recipeId={}", userId, recipeId);
        return recipeService.deleteRecipe(recipeId);
    }
}
