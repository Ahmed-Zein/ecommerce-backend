package com.github.ahmed_zein.ecommerce_backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String key;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryInSeconds}")
    private int expiry;

    private Algorithm algorithm;
    private final String username_key = "USERNAME";

    @PostConstruct
    public void postConstruct() {
        this.algorithm = Algorithm.HMAC256(key);
    }

    public String generateJWT(LocalUser user) {
        return JWT.create()
                .withClaim(username_key, user.getFirstName())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiry * 1000L))
                .sign(algorithm);
    }
}
