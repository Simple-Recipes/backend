package com.recipes.dao;

import com.recipes.entity.Like;
import com.recipes.entity.LikeId;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class LikeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveLike(Like like) {
        entityManager.persist(like);
    }


    @Transactional
    public void deleteLike(LikeId likeId) {
        Like like = entityManager.find(Like.class, likeId);
        if (like != null) {
            entityManager.remove(like);
        }
    }
    public Like findLikeById(LikeId likeId) {
        return entityManager.find(Like.class, likeId);
    }

    public long countLikesByRecipeId(Long recipeId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.recipe.id = :recipeId";
        return ((Number) entityManager.createQuery(query)
                .setParameter("recipeId", recipeId)
                .getSingleResult()).intValue();
    }

    public List<Like> findAllByUserId(Long userId) {
        String query = "SELECT l FROM Like l WHERE l.user.id = :userId";
        return entityManager.createQuery(query, Like.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public boolean existsById(LikeId likeId) {
        String query = "SELECT COUNT(l) FROM Like l WHERE l.id.userId = :userId AND l.id.recipeId = :recipeId";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userId", likeId.getUserId())
                .setParameter("recipeId", likeId.getRecipeId())
                .getSingleResult();
        return count > 0;
    }
}