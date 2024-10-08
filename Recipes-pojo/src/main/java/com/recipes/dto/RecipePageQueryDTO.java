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
@Schema(description = "Recipe page query data transfer object")
public class RecipePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Page number")
    private int page;

    @Schema(description = "Number of records per page")
    private int pageSize;

    @Schema(description = "Search keyword for the recipe title")
    private String searchKeyword;
}
