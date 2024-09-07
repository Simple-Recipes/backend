package com.recipes.dao;

import com.recipes.dto.RecipePageQueryDTO;
import com.recipes.entity.Recipe;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public class RecipeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveRecipe(Recipe recipe) {
        entityManager.persist(recipe);
    }

    @Transactional
    public void updateRecipe(Recipe recipe) {
        entityManager.merge(recipe);
    }

    public Recipe findRecipeById(Long id) {
        return entityManager.find(Recipe.class, id);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = findRecipeById(id);
        if (recipe != null) {
            entityManager.remove(recipe);
        }
    }

    @Transactional
    public void deleteRecipe(Recipe recipe) {
        entityManager.remove(recipe);
    }

    public boolean existsByIdAndUserId(Long id, Long userId) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Recipe r WHERE r.id = :id AND r.user.id = :userId", Long.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }

    public List<Recipe> findPopularRecipes() {
        String sql = "SELECT r.* " +
                "FROM Recipes r " +
                "LEFT JOIN (SELECT l.recipe_id, COUNT(l.user_id) as likes FROM `Like` l GROUP BY l.recipe_id) l ON r.id = l.recipe_id " +
                "LEFT JOIN (SELECT c.recipe_id, COUNT(c.id) as comments FROM Comment c GROUP BY c.recipe_id) c ON r.id = c.recipe_id " +
                "ORDER BY l.likes DESC, c.comments DESC";
        return entityManager.createNativeQuery(sql, Recipe.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Recipe> searchRecipes(RecipePageQueryDTO queryDTO, String sortBy, String tagName) {
        StringBuilder jpql = new StringBuilder("SELECT r FROM Recipe r WHERE 1=1");

        if (queryDTO.getSearchKeyword() != null && !queryDTO.getSearchKeyword().isEmpty()) {
            jpql.append(" AND r.title LIKE :keyword");
        }
        if (tagName != null && !tagName.isEmpty()) {
            jpql.append(" AND EXISTS (SELECT rt FROM RecipeTag rt JOIN rt.tag t WHERE rt.recipe.id = r.id AND t.name = :tagName)");
        }
        if (sortBy != null) {
            if ("likes".equals(sortBy)) {
                jpql.append(" ORDER BY (SELECT COUNT(l.user_id) FROM `Like` l WHERE l.recipe_id = r.id) DESC");
            } else if ("comments".equals(sortBy)) {
                jpql.append(" ORDER BY (SELECT COUNT(c.id) FROM Comment c WHERE c.recipe_id = r.id) DESC");
            }
        } else {
            jpql.append(" ORDER BY r.createTime DESC");
        }

        TypedQuery<Recipe> query = entityManager.createQuery(jpql.toString(), Recipe.class);
        if (queryDTO.getSearchKeyword() != null && !queryDTO.getSearchKeyword().isEmpty()) {
            query.setParameter("keyword", "%" + queryDTO.getSearchKeyword() + "%");
        }
        if (tagName != null && !tagName.isEmpty()) {
            query.setParameter("tagName", tagName);
        }

        return query.setFirstResult((queryDTO.getPage() - 1) * queryDTO.getPageSize())
                .setMaxResults(queryDTO.getPageSize())
                .getResultList();
    }

    public long countSearchRecipes(RecipePageQueryDTO queryDTO, String tagName) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(r) FROM Recipe r WHERE 1=1");

        if (queryDTO.getSearchKeyword() != null && !queryDTO.getSearchKeyword().isEmpty()) {
            jpql.append(" AND r.title LIKE :keyword");
        }
        if (tagName != null && !tagName.isEmpty()) {
            jpql.append(" AND EXISTS (SELECT rt FROM RecipeTag rt JOIN rt.tag t WHERE rt.recipe.id = r.id AND t.name = :tagName)");
        }

        TypedQuery<Long> query = entityManager.createQuery(jpql.toString(), Long.class);
        if (queryDTO.getSearchKeyword() != null && !queryDTO.getSearchKeyword().isEmpty()) {
            query.setParameter("keyword", "%" + queryDTO.getSearchKeyword() + "%");
        }
        if (tagName != null && !tagName.isEmpty()) {
            query.setParameter("tagName", tagName);
        }

        return query.getSingleResult();
    }

    public List<Recipe> findRecipesByUserId(Long userId) {
        return entityManager.createQuery("SELECT r FROM Recipe r WHERE r.user.id = :userId", Recipe.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Recipe> findAllRecipes(RecipePageQueryDTO queryDTO) {
        String jpql = "SELECT r FROM Recipe r ORDER BY r.createTime DESC";
        TypedQuery<Recipe> query = entityManager.createQuery(jpql, Recipe.class);
        return query.setFirstResult((queryDTO.getPage() - 1) * queryDTO.getPageSize())
                .setMaxResults(queryDTO.getPageSize())
                .getResultList();
    }

    public long countAllRecipes(RecipePageQueryDTO queryDTO) {
        String jpql = "SELECT COUNT(r) FROM Recipe r";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    public Long findUserIdByRecipeId(Long recipeId) {
        String jpql = "SELECT r.user.id FROM Recipe r WHERE r.id = :recipeId";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("recipeId", recipeId)
                .getSingleResult();
    }

    public String findTitleById(Long recipeId) {
        String jpql = "SELECT r.title FROM Recipe r WHERE r.id = :recipeId";
        return entityManager.createQuery(jpql, String.class)
                .setParameter("recipeId", recipeId)
                .getSingleResult();
    }


}
