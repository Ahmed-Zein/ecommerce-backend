package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.dao.LocalUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final LocalUserDAO localUserDAO;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    @Autowired
    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
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
        user.setPassword(encryptionService.hashPassword(registrationBody.getPassword()));
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isEmpty()) {
            return null;
        }
        LocalUser user = opUser.get();
        if (!encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
            return null;
        }
        return jwtService.generateJWT(user);
    }
}
