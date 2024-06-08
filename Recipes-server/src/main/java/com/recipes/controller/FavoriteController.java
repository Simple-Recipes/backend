package com.recipes.controller;

import com.recipes.dto.FavoriteDTO;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Result<FavoriteDTO>> addToFavorites(
            @Parameter(description = "Request data containing recipeId", required = true)
            @RequestBody Map<String, Long> request) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        Long recipeId = request.get("recipeId");
        log.info("Adding recipe to favorites: userId={}, recipeId={}", userId, recipeId);
        Result<FavoriteDTO> result = favoriteService.addToFavorites(userId, recipeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove recipe from favorites", description = "Removes a recipe from the user's favorite list")
    public ResponseEntity<Result<Void>> removeFromFavorites(
            @Parameter(description = "Request data containing recipeId", required = true, in = ParameterIn.QUERY)
            @RequestParam Long recipeId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        log.info("Removing recipe from favorites: userId={}, recipeId={}", userId, recipeId);
        Result<Void> result = favoriteService.removeFromFavorites(userId, recipeId);
        HttpStatus status = result.getCode() == 1 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(result, status);
    }
}
