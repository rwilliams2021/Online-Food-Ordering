package com.richard.service;

import com.richard.config.JwtProvider;
import com.richard.model.User;
import com.richard.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private JwtProvider jwtProvider;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testFindUserByJwtToken() throws Exception {
        String jwt = "test.jwt.token";
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        
        when(jwtProvider.getEmailFromJwtToken(anyString())).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        
        Optional<User> result = userService.findUserByJwtToken(jwt);
        
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
    
    @Test
    public void testFindUserByJwtTokenUserNotFound() throws Exception {
        String jwt = "test.jwt.token";
        String email = "test@example.com";
        
        when(jwtProvider.getEmailFromJwtToken(anyString())).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
        Optional<User> result = userService.findUserByJwtToken(jwt);
        
        assertFalse(result.isPresent());
    }
    
    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        
        List<User> result = userService.findAllUsers();
        
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }
    
    @Test
    public void testDeleteUser() {
        Long userId = 1000L;
        
        userService.deleteUser(userId);
        
        verify(userRepository, times(1)).deleteById(userId);
    }
}
