package com.github.ahmed_zein.ecommerce_backend.api.security;

import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.dao.LocalUserDAO;
import com.github.ahmed_zein.ecommerce_backend.service.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityFilterTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private LocalUserDAO localUserDAO;
    @Autowired
    private JWTService jwtService;
    private final static String AUTHENTICATED_PATH = "/auth/me";

    @Test
    public void testUnAuthenticatedRequest() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadToken() throws Exception {
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", "Bearer BAD_TOKEN"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUnVerifiedUser() throws Exception {
        LocalUser user = localUserDAO.findById(2L).get();
        String bearer = "Bearer ";
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", bearer + token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAuthenticatedRequest() throws Exception {
        LocalUser user = localUserDAO.findById(1L).get();
        String bearer = "Bearer ";
        String token = jwtService.generateJWT(user);
        mvc.perform(get(AUTHENTICATED_PATH).header("Authorization", bearer + token))
                .andExpect(status().isOk());
    }
}
