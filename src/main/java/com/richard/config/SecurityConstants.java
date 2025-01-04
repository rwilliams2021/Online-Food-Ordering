package com.richard.config;

import java.util.List;

public class SecurityConstants {
    public static final List<String> PUBLIC_ENDPOINTS = List.of("/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/openapi-schema/**",
                                                                "/v3/api-docs.yaml",
                                                                "/actuator/**",
                                                                "/api/v1/auth/signup",
                                                                "/api/v1/auth/signin",
                                                                "/error");
    
    private SecurityConstants() {
    }
}

