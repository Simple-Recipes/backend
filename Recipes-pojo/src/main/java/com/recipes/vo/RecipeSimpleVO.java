package com.recipes.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified Recipe view object")
public class RecipeSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Recipe ID")
    private Long id;

    @Schema(description = "Recipe Title")
    private String title;

    @Schema(description = "Number of Likes")
    private Long likes;

    @Schema(description = "Recipe Link")
    private String link;
}

