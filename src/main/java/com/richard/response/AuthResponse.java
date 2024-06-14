package com.richard.response;

import com.richard.model.User;
import com.richard.model.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private User user;
    private String message;
    private UserRole role;
}
