package com.recipes.entity;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class RecipeTagId implements Serializable {

    private Long recipeId;
    private Long tagId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeTagId that = (RecipeTagId) o;
        return Objects.equals(recipeId, that.recipeId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, tagId);
    }
}
