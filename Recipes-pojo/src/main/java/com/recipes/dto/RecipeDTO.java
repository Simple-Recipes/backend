package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recipe data transfer object")
public class RecipeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Recipe ID")
    private Long id;

    @Schema(description = "Title", required = true)
    private String title;

    @Schema(description = "Ingredients", required = true)
    private String[] ingredients;

    @Schema(description = "Directions", required = true)
    private String[] directions;

    @Schema(description = "Link to the recipe")
    private String link;

    @Schema(description = "Source of the recipe", required = true)
    private String source;

    @Schema(description ="NER (Named Entity Recognition) tags", required = true)
    private String[] ner;

    private Long userId;

    private String createTime;

    private String updateTime;
    }
