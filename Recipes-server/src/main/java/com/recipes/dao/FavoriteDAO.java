package com.recipes.dao;

import com.recipes.entity.Favorite;
import com.recipes.entity.FavoriteId;
import org.springframework.beans.factory.annotation.Autowired;
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
        entityManager.persist(favorite);
    }

    public Favorite findFavoriteById(FavoriteId id) {
        return entityManager.find(Favorite.class, id);
    }

    @Transactional
    public void deleteFavorite(Favorite favorite) {
        if (entityManager.contains(favorite)) {
            entityManager.remove(favorite);
        } else {
            Favorite managedFavorite = entityManager.find(Favorite.class, favorite.getId());
            if (managedFavorite != null) {
                entityManager.remove(managedFavorite);
            }
        }
    }

    public boolean existsById(FavoriteId id) {
        return entityManager.createQuery("SELECT COUNT(f) FROM Favorite f WHERE f.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult() > 0;
    }
}
