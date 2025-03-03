package org.data.repository.role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class RoleRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public RoleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void createRole(Role role) {
        try {
            Objects.requireNonNull(role);
            entityManager.persist(role);
        } catch (Exception e) {
            throw new RuntimeException("Error inserting role: " + e.getMessage(), e);
        }
    }

    public List<Role> getRoles() {
        try {
            String sql = "SELECT r FROM Role r JOIN Permission p ON p.role.id = r.id JOIN User u ON u.id = r.user.id";
            TypedQuery<Role> query = entityManager.createQuery(sql, Role.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error inserting role: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteRole(Integer id) {
        try {
            Role foundRole = entityManager.find(Role.class, id);
            if (foundRole != null) {
                entityManager.remove(foundRole);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing role: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateRole(Role role) {
        try {
            Objects.requireNonNull(role);
            entityManager.merge(role);
        } catch (Exception e) {
            throw new RuntimeException("Error updating role: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Role getRoleById(Integer id) {
        try {
            Objects.requireNonNull(id);
            Role foundRole = entityManager.find(Role.class, id);
            return foundRole;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching role: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Role getUserRole(Integer userId) {
        try {
            Objects.requireNonNull(userId);
            String sql = "SELECT r FROM Role r WHERE r.user.id=:userId";
            TypedQuery<Role> query = entityManager.createQuery(sql, Role.class);
            query.setParameter("userId", userId);
            Role foundRole = query.getSingleResult();
            return foundRole;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching role: " + e.getMessage(), e);
        }
    }
}
