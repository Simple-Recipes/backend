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
@Schema(description = "Comment data transfer object")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Comment ID")
    private Long id;

    @Schema(description = "Recipe ID", required = true)
    private Long recipeId;

    @Schema(description = "User ID", required = true)
    private Long userId;

    @Schema(description = "Comment content", required = true)
    private String content;

    @Schema(description = "Creation time")
    private String createTime;
}


