package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.dao.LocalUserDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;

    public UserService(LocalUserDAO localUserDAO) {
        this.localUserDAO = localUserDAO;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExists {

        if (localUserDAO.existsByEmailIgnoreCase(registrationBody.getEmail()) || localUserDAO.existsByUsernameIgnoreCase(registrationBody.getUserName())) {
            throw new UserAlreadyExists();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUserName());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        // TODO: Encrypt password!!
        user.setPassword(registrationBody.getPassword());
        return localUserDAO.save(user);
    }
}
