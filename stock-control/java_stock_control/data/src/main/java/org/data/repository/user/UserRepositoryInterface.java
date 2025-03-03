package org.data.repository.user;

import org.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryInterface extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true, value = "SELECT u.id AS user_id, u.name AS user_name, u.password, " +
            "r.id AS role_id, r.name AS role_name, " +
            "p.id, p.permissions " +
            "FROM users u " +
            "JOIN roles r ON r.user_id = u.id " +
            "JOIN permissions p on r.id=p.role_id " +
            "WHERE u.name = :userName")
    public Object userRole(@Param("userName") String userName);
}
