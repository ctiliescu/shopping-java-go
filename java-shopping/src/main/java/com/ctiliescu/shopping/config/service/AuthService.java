package com.ctiliescu.shopping.config.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ctiliescu.shopping.config.model.Credential;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {
    private static Set<String> blackList = new HashSet<>();
    private static String key = "secret" + UUID.randomUUID();

    public String generateToken(int userId) {
        String token = "";

        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            token = JWT.create()
                    .withIssuer("shopping")
                    .withClaim("userId", userId)
                    .withClaim("jwtId", UUID.randomUUID().toString())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
        }

        return token;
    }

    public Optional<Credential> checkToken(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        try {
            DecodedJWT decodedJWT = JWT.decode(jwt);
            algorithm.verify(decodedJWT);
            return Optional.of(new Credential(decodedJWT.getClaim("jwtId").asString(), decodedJWT.getClaim("userId").asInt()))
                    .filter(c -> !blackList.contains(c.getJwdId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void logout(String jwtId) {
        blackList.add(jwtId);
    }
}
