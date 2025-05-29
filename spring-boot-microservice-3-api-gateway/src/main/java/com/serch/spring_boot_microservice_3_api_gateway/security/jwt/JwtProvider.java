package com.serch.spring_boot_microservice_3_api_gateway.security.jwt;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import com.serch.spring_boot_microservice_3_api_gateway.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface JwtProvider {
    String generateToken(UserPrincipal auth);

    Authentication getAuthentication(HttpServletRequest request);

    String generateToken(User user);

    boolean isTokenValid(HttpServletRequest request);
}
