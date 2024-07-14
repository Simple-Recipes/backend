package com.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recommendation request data transfer object")
public class RecommendationRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Preferred ingredients", example = "[\"tomato\", \"cheese\"]")
    private String[] ingredients;

    @Schema(description = "Include all ingredients flag", example = "true")
    private boolean includeAllIngredients;

    @Schema(description = "Maximum time for recipe preparation in minutes", example = "30")
    private int maxTime;

    @Schema(description = "Preference type", example = "4")
    private String preference;
}
