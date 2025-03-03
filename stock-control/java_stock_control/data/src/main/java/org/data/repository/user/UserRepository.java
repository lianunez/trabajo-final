package org.data.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.permission.Permission;
import org.domain.role.Role;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepository {
    private final EntityManager entityManager;
    private final UserRepositoryInterface userRepositoryInterface;

    @Autowired
    public UserRepository(EntityManager entityManager, UserRepositoryInterface userRepositoryInterface) {
        this.entityManager = entityManager;
        this.userRepositoryInterface = userRepositoryInterface;
    }

    public List<User> getUsers() {
        try {
            String sql = "SELECT u FROM User u";
            TypedQuery<User> query = entityManager.createQuery(sql, User.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public User createUser(User user) {
        try {
            this.entityManager.persist(user);
            entityManager.flush();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    public User findUser(String user) {
        try {
            String queryString = "SELECT u FROM User u JOIN Role r ON r.user.id = u.id JOIN Permission p ON p.role.id = r.id WHERE u.userName=:userName";
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            query.setParameter("userName", user);
            User foundUser = query.getSingleResult();
            return foundUser;
        } catch (Exception e) {
            throw new RuntimeException("Error in search for user: " + e.getMessage(), e);
        }
    }

    //adhoc method for JWT mapping
    public HashMap<String, String> findByUsername(String username) {
        try {
            User foundUser = findUser(username);
            HashMap<String, String> userDetails = new HashMap<String, String>() {{
                put("name", foundUser.getUserName());
                put("password", foundUser.getPassword());
            }};
            return userDetails;
        } catch (Exception e) {
            throw new RuntimeException("Error in search for user: " + e.getMessage(), e);
        }
    }

    public User findById(Integer id) {
        try {
            Objects.requireNonNull(id);
            User user = entityManager.find(User.class, id);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error in search for user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Objects.requireNonNull(id);
            User foundUser = findById(id);

            if (foundUser != null) {
                entityManager.remove(foundUser);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in search for user: " + e.getMessage(), e);
        }
    }
}
