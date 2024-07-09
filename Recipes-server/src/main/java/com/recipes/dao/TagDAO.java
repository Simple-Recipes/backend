package com.recipes.dao;

import com.recipes.entity.Tag;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class TagDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Tag> findAllTags() {
        String jpql = "SELECT t FROM Tag t";
        return entityManager.createQuery(jpql, Tag.class)
                .getResultList();
    }

    @Transactional
    public void saveTag(Tag tag) {
        entityManager.persist(tag);
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag != null) {
            entityManager.remove(tag);
        }
    }
    public List<Tag> findAllByUserId(Long userId) {
        return entityManager.createQuery(
                        "SELECT t FROM Tag t JOIN RecipeTag rt ON t.id = rt.tag.id JOIN Recipe r ON rt.recipe.id = r.id WHERE r.user.id = :userId", Tag.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public Tag findTagById(Long id) {
        return entityManager.find(Tag.class, id);
    }
}
