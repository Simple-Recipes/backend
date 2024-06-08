package com.recipes.dao;

import com.recipes.entity.Comment;
import com.recipes.entity.Like;
import com.recipes.entity.Tag;
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

    public List<Comment> findCommentsByRecipeId(Long recipeId) {
        String jpql = "SELECT c FROM Comment c WHERE c.recipe.id = :recipeId";
        return entityManager.createQuery(jpql, Comment.class)
                            .setParameter("recipeId", recipeId)
                            .getResultList();
    }
}


