package com.recipes.dao;


import com.recipes.entity.Notification;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public class NotificationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveNotification(Notification notification) {
        entityManager.persist(notification);
    }

    public List<Notification> findNotificationsByRecipientId(Long recipientId) {
        String jpql = "SELECT n FROM Notification n WHERE n.recipientId = :recipientId ORDER BY n.createTime DESC";
        return entityManager.createQuery(jpql, Notification.class)
                .setParameter("recipientId", recipientId)
                .getResultList();
    }

    @Transactional
    public void markAsRead(Long id) {
        Notification notification = entityManager.find(Notification.class, id);
        if (notification != null) {
            notification.setIsRead(true);
            entityManager.merge(notification);
        }
    }
}

