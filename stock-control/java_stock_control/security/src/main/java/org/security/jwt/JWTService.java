package org.security.jwt;

import org.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
    private final JWTUtils jwtUtils;

    @Autowired
    public JWTService(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String generateToken(User user){
        return jwtUtils.generateToken(user);
    }
}
