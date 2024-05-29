package com.richard.response;

import com.richard.model.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserRole role;
}
