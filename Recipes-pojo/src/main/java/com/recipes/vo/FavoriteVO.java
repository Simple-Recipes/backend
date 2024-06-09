package com.recipes.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Favorite view object")
public class FavoriteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description ="User ID")
    private Long userId;

    @Schema(description ="Recipe ID")
    private Long recipeId;

    @Schema(description ="Favorite Creation Time")
    private String createTime;
}
