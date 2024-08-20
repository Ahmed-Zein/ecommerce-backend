package com.github.ahmed_zein.ecommerce_backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    @Value("${encryption.salt.rounds}")
    private int rounds;
    private String salt;

    @PostConstruct
    public void postConstruct() {
        salt = BCrypt.gensalt(rounds);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
