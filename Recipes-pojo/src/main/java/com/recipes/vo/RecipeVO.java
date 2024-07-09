package com.recipes.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recipe view object")
public class RecipeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String[] ingredients;
    private String[] directions;
    private String link;
    private String source;
    private String[] ner;
    private Long userId;
    private String createTime;
    private String updateTime;
}
