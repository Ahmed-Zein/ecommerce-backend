package com.github.ahmed_zein.ecommerce_backend.api.controller.order;

import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.WebOrder;
import com.github.ahmed_zein.ecommerce_backend.model.dao.WebOrderDAO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class WebOrderController {
    final private WebOrderDAO webOrderDAO;

    public WebOrderController(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user) {
        return webOrderDAO.findByUser(user);
    }
}
