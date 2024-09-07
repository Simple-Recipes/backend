// CommentDAO.java
package com.recipes.dao;

import com.recipes.entity.Comment;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class CommentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = entityManager.find(Comment.class, commentId);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }

    public Comment findCommentById(Long commentId) {
        return entityManager.find(Comment.class, commentId);
    }

    public List<Comment> findCommentsByRecipeId(Long recipeId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.recipe.id = :recipeId", Comment.class)
                .setParameter("recipeId", recipeId)
                .getResultList();
    }

    public List<Comment> findCommentsByUserId(Long userId) {
        return entityManager.createQuery("SELECT c FROM Comment c WHERE c.user.id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Long findRecipeCreatorId(Long recipeId) {
        return entityManager.createQuery(
                        "SELECT r.user.id FROM Recipe r WHERE r.id = :recipeId",
                        Long.class)
                .setParameter("recipeId", recipeId)
                .getSingleResult();
    }
}
