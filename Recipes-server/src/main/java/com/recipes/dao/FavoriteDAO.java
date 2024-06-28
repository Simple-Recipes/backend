package com.recipes.dao;

import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import com.recipes.entity.Recipe;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public class FavoriteDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveFavorite(Favorite favorite) {
        entityManager.merge(favorite);
    }

    @Transactional
    public void deleteFavorite(FavoriteId id) {
        Favorite favorite = entityManager.find(Favorite.class, id);
        if (favorite != null) {
            entityManager.remove(favorite);
        }
    }

    public Favorite findFavoriteById(FavoriteId id) {
        return entityManager.find(Favorite.class, id);
    }

    public List<Recipe> findFavoritesByUserId(Long userId) {
        return entityManager.createQuery("SELECT f.recipe FROM Favorite f WHERE f.user.id = :userId", Recipe.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
