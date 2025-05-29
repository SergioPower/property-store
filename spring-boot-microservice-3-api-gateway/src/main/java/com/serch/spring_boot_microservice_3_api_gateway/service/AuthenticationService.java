package com.serch.spring_boot_microservice_3_api_gateway.service;

import com.serch.spring_boot_microservice_3_api_gateway.model.User;

public interface AuthenticationService {
    User singInAndReturnJWT(User singInRequest);
}
