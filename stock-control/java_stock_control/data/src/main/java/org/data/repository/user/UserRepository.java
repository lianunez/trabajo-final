package org.data.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserRepository {
    private final EntityManager entityManager;

    @Autowired
    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User createUser(User user) {
        try {
            this.entityManager.persist(user);
            return user;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public User findUser(String user) {
        try {
            String queryString = "SELECT u FROM User u WHERE u.userName=:userName";
            TypedQuery<User> query = entityManager.createQuery(queryString, User.class);
            query.setParameter("userName", user);
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //adhoc method for JWT mapping
    public HashMap<String, String> findByUsername(String username){
        User foundUser = findUser(username);
        HashMap<String, String> userDetails = new HashMap<String, String>(){{
            put("name", foundUser.getUserName());
            put("password", foundUser.getPassword());
        }};
        return userDetails;
    }
}
