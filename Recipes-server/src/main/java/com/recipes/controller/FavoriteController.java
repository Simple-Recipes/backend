package com.recipes.controller;

import com.recipes.dto.FavoriteDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
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
@RequestMapping("/favorites")
@CrossOrigin(origins = {"http://localhost:8085", "http://localhost:3000","http://localhost:8080"})

@Slf4j
@Tag(name = "Favorite API", description = "Operations related to favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;


    @PostMapping("/add")
    @Operation(summary = "Add recipe to favorites", description = "Adds a recipe to the user's favorite list")
    public ResponseEntity<Result<FavoriteDTO>> addToFavorites(
            @Parameter(description = "Favorite data transfer object", required = true)
            @RequestBody FavoriteDTO favoriteDTO) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        favoriteDTO.setUserId(userId);
        log.info("Adding recipe to favorites: userId={}, recipeId={}", userId, favoriteDTO.getRecipeId());
        Result<FavoriteDTO> result = favoriteService.addToFavorites(favoriteDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remove recipe from favorites", description = "Removes a recipe from the user's favorite list")
    public ResponseEntity<Result<Void>> removeFromFavorites(
            @Parameter(description = "Request data containing recipeId", required = true, in = ParameterIn.QUERY)
            @RequestParam Long recipeId) {
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            log.error("User is not logged in");
            return new ResponseEntity<>(Result.error("User is not logged in"), HttpStatus.UNAUTHORIZED);
        }
        log.info("Removing recipe from favorites: userId={}, recipeId={}", userId, recipeId);
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setUserId(userId);
        favoriteDTO.setRecipeId(recipeId);
        Result<Void> result = favoriteService.removeFromFavorites(favoriteDTO);
        HttpStatus status = result.getCode() == 1 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/getAllMyFavorites")
    @Operation(summary = "Get all my favorites", description = "Get all recipes favorited by the user")
    public Result<List<RecipeDTO>> getAllMyFavorites() {
        Long userId = UserHolder.getUser().getId();
//        if (userId == null) {
//            log.error("User is not logged in");
//            return Result.error("User is not logged in");
//        }
        log.info("Getting all favorites for user with id={}", userId);
        return favoriteService.getAllMyFavorites(userId);
    }
}
