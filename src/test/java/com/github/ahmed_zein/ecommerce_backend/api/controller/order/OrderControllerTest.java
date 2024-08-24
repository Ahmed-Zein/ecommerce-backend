package com.github.ahmed_zein.ecommerce_backend.api.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ahmed_zein.ecommerce_backend.model.WebOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("UserA")
    public void testUserAAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongToUserName("UserA");
    }

    @Test
    @WithUserDetails("UserB")
    public void testUserBAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongToUserName("UserB");
    }

    @Test
    @WithUserDetails("UserC")
    public void testUserCAuthenticatedOrderList() throws Exception {
        testAuthenticatedListBelongToUserName("UserC");
    }

    private void testAuthenticatedListBelongToUserName(String username) throws Exception {
        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<WebOrder> orders = new ObjectMapper()
                            .readValue(json, new TypeReference<List<WebOrder>>() {
                            });
                    orders.forEach(order -> {
                        System.out.println(order);
                        Assertions.assertEquals(username, order.getUser().getUsername(), "Order List Should belong to " + username);
                    });
                });
    }

    @Test
    public void testUnAuthenticatedOrderList() throws Exception {
        mvc.perform(get("/orders")).andExpect(status().isForbidden());

    }

}
