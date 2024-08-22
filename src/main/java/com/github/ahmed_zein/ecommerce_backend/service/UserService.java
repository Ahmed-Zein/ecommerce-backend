package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.EmailFailureException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExists;
import com.github.ahmed_zein.ecommerce_backend.exception.UserNotVerifiedException;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.VerificationToken;
import com.github.ahmed_zein.ecommerce_backend.model.dao.LocalUserDAO;
import com.github.ahmed_zein.ecommerce_backend.model.dao.VerificationTokenDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserService {

    private final JWTService jwtService;
    private final EmailService emailService;
    private final LocalUserDAO localUserDAO;
    private final EncryptionService encryptionService;
    private final VerificationTokenDAO verificationTokenDAO;

    @Autowired
    public UserService(LocalUserDAO localUserDAO, EncryptionService encryptionService, EmailService emailService, JWTService jwtService,
                       VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExists, EmailFailureException {

        if (localUserDAO.existsByEmailIgnoreCase(registrationBody.getEmail()) || localUserDAO.existsByUsernameIgnoreCase(registrationBody.getUserName())) {
            throw new UserAlreadyExists();
        }
        LocalUser user = new LocalUser();
        user.setUsername(registrationBody.getUserName());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        user.setPassword(encryptionService.hashPassword(registrationBody.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
        verificationTokenDAO.save(verificationToken);
        return localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException {
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isEmpty()) {
            return null;
        }
        LocalUser user = opUser.get();
        if (!encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
            return null;
        }
        if (user.isEmailVerified()) {
            return jwtService.generateJWT(user);
        }
        boolean resend = user.getVerificationTokens().isEmpty() ||
                user.getVerificationTokens().get(0).getCreatedTimeStamp().before(new Timestamp(System.currentTimeMillis() - (60 * 60 * 1000)));
        if (resend) {
            VerificationToken verificationToken = createVerificationToken(user);
            verificationTokenDAO.save(verificationToken);
            emailService.sendVerificationEmail(verificationToken);
        }
        throw new UserNotVerifiedException(resend);
    }

    @Transactional
    public Boolean verifyEmail(String token) {
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isEmpty()) {
            return false;
        }
        VerificationToken verificationToken = opToken.get();
        LocalUser user = verificationToken.getUser();
        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            localUserDAO.save(user);
        }
        verificationTokenDAO.delete(verificationToken);
        return true;
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationToken(user));
        verificationToken.setCreatedTimeStamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }
}
