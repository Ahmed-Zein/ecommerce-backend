package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.WebOrder;
import com.github.ahmed_zein.ecommerce_backend.model.dao.WebOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebOrderService {
    public final WebOrderDAO webOrderDAO;

    public WebOrderService(WebOrderDAO webOrderDAO) {
        this.webOrderDAO = webOrderDAO;
    }

    public List<WebOrder> getWebOrder(LocalUser user) {
        return webOrderDAO.findByUser(user);
    }
}
