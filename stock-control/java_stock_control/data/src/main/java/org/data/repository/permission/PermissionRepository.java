package org.data.repository.permission;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class PermissionRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public PermissionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createPermission(Permission permission) {
        try {
            Objects.requireNonNull(permission);
            entityManager.persist(permission);
        } catch (Exception e) {
            throw new RuntimeException("Error inserting permission: " + e.getMessage(), e);
        }
    }

    public List<Permission> getPermissions() {
        try {
            String sql = "SELECT p FROM Permission p";
            TypedQuery<Permission> query = entityManager.createQuery(sql, Permission.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error inserting permissions: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePermission(String id) {
        try {
            Permission foundPermission = entityManager.find(Permission.class, String.valueOf(id));
            if (foundPermission != null) {
                entityManager.remove(foundPermission);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing permissions: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updatePermission(Permission permission) {
        try {
            Objects.requireNonNull(permission);
            entityManager.merge(permission);
        } catch (Exception e) {
            throw new RuntimeException("Error updating permission: " + e.getMessage(), e);
        }
    }
}
