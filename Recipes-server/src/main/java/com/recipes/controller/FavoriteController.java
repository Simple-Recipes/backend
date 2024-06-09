package com.recipes.controller;

import com.recipes.dto.FavoriteDTO;
import com.recipes.result.Result;
import com.recipes.service.FavoriteService;
import io.swagger.annotations.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
@Slf4j
@Api(tags = "Favorite API")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private HttpSession session; // Autowire the HttpSession

    @PostMapping("/add")
    @ApiOperation(value = "Add recipe to favorites", notes = "Adds a recipe to the user's favorite list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value = "Request data containing recipeId", required = true, dataType = "Map", paramType = "body", example = "{\"recipeId\": 123}")
    })
    public Result<FavoriteDTO> addToFavorites(
            @ApiParam(value = "Request data containing recipeId", required = true)
            @RequestBody Map<String, Long> request) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        Long recipeId = request.get("recipeId");
        log.info("Adding recipe to favorites: userId={}, recipeId={}", userId, recipeId);
        return favoriteService.addToFavorites(recipeId);
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "Remove recipe from favorites", notes = "Removes a recipe from the user's favorite list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request", value = "Request data containing recipeId", required = true, dataType = "Map", paramType = "body", example = "{\"recipeId\": 123}")
    })
    public Result<Void> removeFromFavorites(
            @ApiParam(value = "Request data containing recipeId", required = true)
            @RequestBody Map<String, Long> request) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            log.error("User is not logged in");
            return Result.error("User is not logged in");
        }
        Long recipeId = request.get("recipeId");
        log.info("Removing recipe from favorites: userId={}, recipeId={}", userId, recipeId);
        return favoriteService.removeFromFavorites(recipeId);
    }
}
