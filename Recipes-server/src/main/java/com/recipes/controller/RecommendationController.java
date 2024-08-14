package com.recipes.controller;

import com.recipes.dto.RecommendationRequestDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;
import com.recipes.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendation")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5000","http://localhost:8082"})
@Tag(name = "Recommendation API", description = "Operations related to recipe recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    @Operation(summary = "Get recipe recommendations", description = "Get a list of recommended recipes based on user preferences")
    public Result<List<RecipeDTO>> getRecommendations(@RequestBody RecommendationRequestDTO requestDTO) {
        return recommendationService.getRecommendations(requestDTO);
    }
}
