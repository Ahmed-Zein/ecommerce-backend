package com.github.ahmed_zein.ecommerce_backend.model.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.github.ahmed_zein.ecommerce_backend.model.VerificationToken;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);
}
