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
@Schema(description = "Tag data transfer object")
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Tag ID")
    private Long id;

    @Schema(description = "Tag name", required = true)
    private String name;
}
