package com.recipes.dao;

import com.recipes.entity.Recipe;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class RecipeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveRecipe(Recipe recipe) {
        entityManager.persist(recipe);
    }

    @Transactional
    public void updateRecipe(Recipe recipe) {
        entityManager.merge(recipe);
    }

    public Recipe findRecipeById(Long id) {
        return entityManager.find(Recipe.class, id);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = findRecipeById(id);
        if (recipe != null) {
            entityManager.remove(recipe);
        }
    }

    public boolean existsById(Long id) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Recipe r WHERE r.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult() > 0;
    }
}
