package com.github.ahmed_zein.ecommerce_backend.model.dao;

import com.github.ahmed_zein.ecommerce_backend.model.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface AddressDAO extends ListCrudRepository<Address, Long> {
    List<Address> findByUser_Id(Long id);
}
