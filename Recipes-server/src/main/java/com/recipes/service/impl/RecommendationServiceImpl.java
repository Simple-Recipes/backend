package com.recipes.service.impl;

import com.recipes.dao.RecipeDAO;
import com.recipes.dto.RecommendationRequestDTO;
import com.recipes.dto.RecipeDTO;
import com.recipes.mapper.RecipeMapper;
import com.recipes.service.RecommendationService;
import com.recipes.utils.ProcessUtils;
import com.recipes.result.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    @Value("${python.script.path}")
    private String pythonScriptPath;

    private final RecipeDAO recipeDAO;
    private final RecipeMapper recipeMapper;

    public RecommendationServiceImpl(RecipeDAO recipeDAO, RecipeMapper recipeMapper) {
        this.recipeDAO = recipeDAO;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public Result<List<RecipeDTO>> getRecommendations(RecommendationRequestDTO requestDTO) {
        log.info("getRecommendations");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonArgs = objectMapper.writeValueAsString(requestDTO);
            log.info("Arguments: {}", jsonArgs);
            log.info("Ready to execute python script");
            String output = ProcessUtils.runPythonScript(pythonScriptPath, jsonArgs);
            log.info("Python script output: {}", output);

            if (output.isEmpty()) {
                log.error("Python script returned empty output");
                return Result.error("Python script returned empty output");
            }

            List<Long> recipeIds = objectMapper.readValue(output, new TypeReference<List<Long>>() {});
            log.info("Recommended recipe IDs: {}", recipeIds);

            List<RecipeDTO> recommendations = recipeIds.stream()
                    .map(recipeDAO::findRecipeById)
                    .map(recipeMapper::toDto)
                    .collect(Collectors.toList());
            log.info("Recommendations: {}", recommendations);
            return Result.success(recommendations);
        } catch (Exception e) {
            log.error("Failed to get recommendations", e);
            return Result.error("Failed to get recommendations");
        }
    }
}
