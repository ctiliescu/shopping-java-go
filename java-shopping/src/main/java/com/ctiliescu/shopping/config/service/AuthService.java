package com.ctiliescu.shopping.config.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
    private static Set<String> blackList = new HashSet<>();
    private static String key = "secret" + UUID.randomUUID();

    public String generateToken(int userId) {
        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            token = JWT.create()
                    .withIssuer("shopping")
                    .withClaim("userId", userId)
                    .withClaim("jwtId", UUID.randomUUID().toString())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
        }

        return token;
    }

    public void logout(String jwtId) {
        blackList.add(jwtId);
    }
}
