package com.github.ahmed_zein.ecommerce_backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testEncryptionService() {
        String password = "password123";
        String hash = encryptionService.hashPassword(password);
        assertTrue(encryptionService.verifyPassword(password, hash), "hashed password should match the original ");
        assertFalse(encryptionService.verifyPassword("WRONG_PASSWORD", hash), "Wrong password should not be valid");
    }

}
