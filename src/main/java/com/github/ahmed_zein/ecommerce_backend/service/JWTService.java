package com.github.ahmed_zein.ecommerce_backend.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;

import jakarta.annotation.PostConstruct;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String key;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiryInSeconds}")
    private int expiry;

    private Algorithm algorithm;
    private static final String USERNAME_KEY = "USERNAME";
    private static final String EMAIL_VERIFICATION_KEY = "EMAIL_VERIFICATION";
    private static final String PASSWORD_RESET_KEY = "PASSWORD_RESET";

    @PostConstruct
    public void postConstruct() {
        this.algorithm = Algorithm.HMAC256(key);
    }

    public String generateJWT(LocalUser user) {
        return JWT.create()
                .withClaim(USERNAME_KEY, user.getUsername())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiry * 1000L))
                .sign(algorithm);
    }

    public String generateVerificationToken(LocalUser user) {
        return JWT.create()
                .withClaim(EMAIL_VERIFICATION_KEY, user.getEmail())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiry * 1000L))
                .sign(algorithm);
    }

    public String generatePasswordRestJWT(LocalUser user) {
        return JWT.create()
                .withClaim(PASSWORD_RESET_KEY, user.getEmail())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 15))
                .sign(algorithm);
    }

    public String getEmail(String token) {
        DecodedJWT decodedToken = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedToken.getClaim(PASSWORD_RESET_KEY).asString();
    }

    public String getUsername(String token) {
        DecodedJWT decodedToken = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedToken.getClaim(USERNAME_KEY).asString();
    }
}
