package com.recipes.controller;

import com.recipes.dto.FavoriteDTO;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Tag(name = "Favorite API", description = "Operations related to favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private HttpSession session;

    @PostMapping("/add")
    @Operation(summary = "Add recipe to favorites", description = "Adds a recipe to the user's favorite list")
    public Result<FavoriteDTO> addToFavorites(
            @Parameter(description = "Request data containing recipeId", required = true)
            @RequestBody Map<String, Long> request) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        Long recipeId = request.get("recipeId");
        log.info("Adding recipe to favorites: userId={}, recipeId={}", userId, recipeId);
        return favoriteService.addToFavorites(userId, recipeId);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove recipe from favorites", description = "Removes a recipe from the user's favorite list")
    public Result<Void> removeFromFavorites(
            @Parameter(description = "Request data containing recipeId", required = true, in = ParameterIn.QUERY)
            @RequestParam Long recipeId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        log.info("Removing recipe from favorites: userId={}, recipeId={}", userId, recipeId);
        return favoriteService.removeFromFavorites(userId, recipeId);
    }
}
