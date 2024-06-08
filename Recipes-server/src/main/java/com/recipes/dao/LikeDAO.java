package com.recipes.dao;


import com.recipes.entity.Like;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class LikeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveLike(Like like) {
        entityManager.persist(like);
    }

    @Transactional
    public void deleteLike(Like like) {
        String jpql = "DELETE FROM Like l WHERE l.user.id = :userId AND l.recipe.id = :recipeId";
        entityManager.createQuery(jpql)
                     .setParameter("userId", like.getUser().getId())
                     .setParameter("recipeId", like.getRecipe().getId())
                     .executeUpdate();
    }

    public int countLikesByRecipeId(Long recipeId) {
        String jpql = "SELECT COUNT(l) FROM Like l WHERE l.recipe.id = :recipeId";
        return ((Long) entityManager.createQuery(jpql)
                                    .setParameter("recipeId", recipeId)
                                    .getSingleResult()).intValue();
    }
}

