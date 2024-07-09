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
@Schema(description = "Favorite data transfer object")
public class FavoriteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Schema(description="User ID")
    private Long userId;

    @Schema(description="Recipe ID", required = true)
    private Long recipeId;

    @Schema(description="Favorite Creation Time")
    private String createTime;
}
