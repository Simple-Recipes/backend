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
@Schema(description = "RecipeTag view object")
public class RecipeTagVO implements Serializable {

    private Long recipeId;
    private Long tagId;
}
