package com.github.ahmed_zein.ecommerce_backend.model.dao;

import com.github.ahmed_zein.ecommerce_backend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
