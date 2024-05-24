package com.richard.service;

import com.richard.model.User;

public interface UserService {
    
    User findUserByJwtToken(String token) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
