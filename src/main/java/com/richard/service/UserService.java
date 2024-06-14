package com.richard.service;

import com.richard.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    Optional<User> findUserByJwtToken(String token) throws Exception;
    List<User> findAllUsers();
    void deleteUser(Long id);
}
