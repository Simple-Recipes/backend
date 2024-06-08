package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Like data transfer object")
public class LikeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID", required = true)
    private Long userId;

    @Schema(description = "Recipe ID", required = true)
    private Long recipeId;
}
