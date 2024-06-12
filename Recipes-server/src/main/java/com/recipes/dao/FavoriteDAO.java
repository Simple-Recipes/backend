package com.recipes.dao;

import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class FavoriteDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveFavorite(Favorite favorite) {
        entityManager.persist(favorite);
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
}
