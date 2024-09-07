package com.recipes.dao;


import com.recipes.entity.Inventory;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveInventory(Inventory inventory) {
        entityManager.persist(inventory);
    }

    @Transactional
    public void deleteInventory(Long inventoryId) {
        Inventory inventory = entityManager.find(Inventory.class, inventoryId);
        if (inventory != null) {
            entityManager.remove(inventory);
        }
    }

    public Inventory findInventoryById(Long id) {
        return entityManager.find(Inventory.class, id);
    }

    public List<Inventory> findInventoriesByUserId(Long userId) {
        return entityManager.createQuery("SELECT i FROM Inventory i WHERE i.user.id = :userId", Inventory.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return entityManager.createQuery("SELECT COUNT(i) FROM Inventory i WHERE i.id = :id AND i.user.id = :userId", Long.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }
}

