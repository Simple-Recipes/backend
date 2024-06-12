package com.recipes.dao;

import com.recipes.entity.User;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    public User findUserByUsername(String username) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public User findUserByEmail(String email) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public User findUserByResetToken(String resetToken) {
        // Placeholder method, replace with actual query to find user by reset token
        // This assumes a resetToken field exists in the User entity
        //
        List<User> users = entityManager.createQuery("SELECT u FROM User u WHERE u.password = :resetToken", User.class)
                .setParameter("resetToken", resetToken)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
