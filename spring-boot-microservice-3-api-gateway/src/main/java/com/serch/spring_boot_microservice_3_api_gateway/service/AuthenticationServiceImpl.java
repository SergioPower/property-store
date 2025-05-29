package com.serch.spring_boot_microservice_3_api_gateway.service;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;
import com.serch.spring_boot_microservice_3_api_gateway.security.UserPrincipal;
import com.serch.spring_boot_microservice_3_api_gateway.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User singInAndReturnJWT(User singInRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(singInRequest.getUsername(),
                        singInRequest.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);

        User sigInUser = userPrincipal.getUser();
        sigInUser.setToken(jwt);

        return sigInUser;

    }
}
