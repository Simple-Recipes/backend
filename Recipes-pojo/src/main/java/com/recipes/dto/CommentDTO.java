package com.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Comment data transfer object")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "The unique ID of the comment")
    private Long id;

    @Schema(description = "The ID of the recipe", required = true)
    private Long recipeId;

    @Schema(description = "The ID of the user")
    private Long userId;

    @Schema(description = "The content of the comment", required = true)
    private String content;

    @Schema(description = "The creation time of the comment")
    private LocalDateTime createTime;
}


