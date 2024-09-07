package com.recipes.dao;

import com.recipes.entity.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AdminDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Admin findAdminByName(String username){
        List<Admin> admins = entityManager.createQuery
                ("SELECT a FROM Admin a WHERE a.username = :username", Admin.class)
                .setParameter("username", username)
                .getResultList();
        return admins.isEmpty() ? null : admins.get(0);

    }

}
