package com.github.ahmed_zein.ecommerce_backend.model.dao;

import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import org.springframework.data.repository.CrudRepository;

public interface LocalUserDAO extends CrudRepository<LocalUser, Long> {
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);
}
