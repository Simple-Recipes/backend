package com.recipes.dao;
import com.recipes.entity.ShoppingList;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class ShoppingListDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveShoppingList(ShoppingList shoppingList) {
        if (shoppingList.getId() == null) {
            entityManager.persist(shoppingList);  // Persist new entities
        } else {
            entityManager.merge(shoppingList);    // Merge detached entities for updates
        }
    }

    @Transactional
    public void deleteShoppingList(Long shoppingListId) {
        ShoppingList shoppingList = entityManager.find(ShoppingList.class, shoppingListId);
        if (shoppingList != null) {
            entityManager.remove(shoppingList);
        }
    }

    public ShoppingList findShoppingListById(Long id){
        return  entityManager.find(ShoppingList.class, id);
    }

    public List<ShoppingList> findShoppingListByUserId(Long userId) {
        return entityManager.createQuery("SELECT s FROM ShoppingList s WHERE s.user.id = :userId", ShoppingList.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return entityManager.createQuery("SELECT COUNT(s) FROM ShoppingList s WHERE s.id = :id AND s.user.id = :userId", Long.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }





}
