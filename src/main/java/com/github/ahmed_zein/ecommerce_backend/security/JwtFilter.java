package com.github.ahmed_zein.ecommerce_backend.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.github.ahmed_zein.ecommerce_backend.model.LocalUser;
import com.github.ahmed_zein.ecommerce_backend.model.dao.LocalUserDAO;
import com.github.ahmed_zein.ecommerce_backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final LocalUserDAO localUserDAO;

    public JwtFilter(JWTService jwtService, LocalUserDAO localUserDAO) {
        this.jwtService = jwtService;
        this.localUserDAO = localUserDAO;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String bearer = "Bearer ";
        if (header == null || !header.contains(bearer)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(bearer.length());
        try {
            String username = jwtService.getUsername(token);
            Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
            if (opUser.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            LocalUser user = opUser.get();
            if (!user.isEmailVerified()) {
                return;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JWTDecodeException e) {
            System.out.println("JWT error:");
            System.err.println(Arrays.toString(e.getStackTrace()));
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
