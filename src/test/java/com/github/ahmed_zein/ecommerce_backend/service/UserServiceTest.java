package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.api.model.LoginBody;
import com.github.ahmed_zein.ecommerce_backend.api.model.RegistrationBody;
import com.github.ahmed_zein.ecommerce_backend.exception.EmailFailureException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserAlreadyExistsException;
import com.github.ahmed_zein.ecommerce_backend.exception.UserNotVerifiedException;
import com.github.ahmed_zein.ecommerce_backend.model.VerificationToken;
import com.github.ahmed_zein.ecommerce_backend.model.dao.VerificationTokenDAO;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @RegisterExtension
    private static final GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("greybeast", "secret"))
            .withPerMethodLifecycle(true);

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenDAO verificationTokenDAO;

    @Test
    @Transactional
    public void testRegisterUser() throws MessagingException {
        RegistrationBody body = RegistrationBody.builder()
                .userName("UserA")
                .email("UserServiceTest$testRegisterUser@junit.com")
                .firstName("FirstName")
                .lastName("LastName")
                .password("MySecretPassword123")
                .build();

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body), "Username should already be in use.");

        body.setUserName("UserServiceTest$testRegisterUser");
        body.setEmail("UserA@junit.com");
        assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerUser(body), "Email should already be in use.");

        body.setEmail("UserServiceTest$testRegisterUser@junit.com");
        assertDoesNotThrow(() -> userService.registerUser(body),
                "User should register successfully.");

        Assertions.assertEquals(body.getEmail(),
                greenMailExtension.getReceivedMessages()[0]
                        .getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, EmailFailureException {
        LoginBody body = LoginBody.builder()
                .username("UserA-NotExist")
                .password("passwordA123-wrongPassword")
                .build();

        assertNull(userService.loginUser(body), "Username shouldn't be found");

        body.setUsername("UserA");
        assertNull(userService.loginUser(body), "Password should be incorrect");

        body.setPassword("passwordA123");
        assertNotNull(userService.loginUser(body), "User should login successfully");

        body.setUsername("UserB");
        body.setPassword("passwordB123");
        try {
            userService.loginUser(body);
            fail("User should not have a verified email");
        } catch (UserNotVerifiedException e) {
            assertTrue(e.isNewEmailSent(), "Email verification should be sent");
            assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
        try {
            userService.loginUser(body);
            fail("User should not have a verified email");
        } catch (UserNotVerifiedException e) {
            assertFalse(e.isNewEmailSent(), "Email verification should be sent");
            assertEquals(1, greenMailExtension.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws UserNotVerifiedException, EmailFailureException {
        assertFalse(userService.verifyUser("BAD_TOKEN"), "Token should not exist");

        LoginBody body = LoginBody.builder()
                .username("UserB")
                .password("passwordB123")
                .build();

        try {
            userService.loginUser(body);
        } catch (UserNotVerifiedException e) {
            List<VerificationToken> tokens = verificationTokenDAO.findByUser_IdOrderByIdDesc(2L);
            String token = tokens.get(0).getToken();
            assertTrue(userService.verifyUser(token), "email should be verified");
            assertNotNull(userService.loginUser(body));
        }
    }
}