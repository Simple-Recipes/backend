package com.recipes.entity;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
@Embeddable
public class RecipeTagId implements Serializable {

    private Long recipeId;
    private Long tagId;

    // getters, setters, equals, hashCode methods

    public RecipeTagId() {
    }
    public RecipeTagId(Long recipeId, Long tagId) {
        this.recipeId = recipeId;
        this.tagId = tagId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
