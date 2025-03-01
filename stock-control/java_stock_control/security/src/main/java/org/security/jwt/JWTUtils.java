package org.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    @Value("${jwt.secret-key}")
    private String secretKey;
    public String generateToken(User user){
        Date from = new Date();
        Date to = new Date(System.currentTimeMillis() + 1000 * 60 * 20);
        return JWT.create()
                .withSubject(user.getUserName())
                .withIssuedAt(from)
                .withExpiresAt(to)
                .sign(Algorithm.HMAC256(this.secretKey));
    }

    public boolean isTokenValid(String token, User user){
        String userName = extractUser(token);
        boolean isUser = userName.equals(user.getUserName());
        boolean isTokenValid =  isTokenValid(token);
        return isUser && isTokenValid;
    }

    public String extractUser(String token){
        return JWT.require(Algorithm.HMAC256(this.secretKey))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean isTokenValid(String token){
        DecodedJWT decoded = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        boolean isValid = decoded.getExpiresAt().after(new Date());

        return isValid;
    }
}
