package com.recipes.service;

import com.recipes.dto.RecommendationRequestDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.result.Result;

import java.util.List;

public interface RecommendationService {
    Result<List<RecipeDTO>> getRecommendations(RecommendationRequestDTO requestDTO);
}
