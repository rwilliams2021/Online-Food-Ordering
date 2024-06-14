package com.richard.controller;

import com.richard.config.JwtProvider;
import com.richard.model.Cart;
import com.richard.model.User;
import com.richard.request.LoginRequest;
import com.richard.response.AuthResponse;
import com.richard.respository.CartRepository;
import com.richard.respository.UserRepository;
import com.richard.service.CustomerUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtProvider jwtProvider;
    
    @Mock
    private CustomerUserDetailsService customerUserDetailsService;
    
    @Mock
    private CartRepository cartRepository;
    
    @InjectMocks
    private AuthController authController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateUserHandler() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFullname("Test User");
        user.setPassword("password");
        
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwtToken");
        
        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("User created successfully.", Objects.requireNonNull(response.getBody()).getMessage());
        verify(userRepository, times(1)).save(any(User.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
    @Test
    public void testSignIn() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
                                                                                    .password("encodedPassword")
                                                                                    .authorities(new SimpleGrantedAuthority(
                                                                                            "CUSTOMER"))
                                                                                    .build();
        
        when(customerUserDetailsService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwtToken");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));
        
        ResponseEntity<AuthResponse> response = authController.signIn(loginRequest);
        
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("User logged in successfully.", Objects.requireNonNull(response.getBody()).getMessage());
        verify(jwtProvider, times(1)).generateToken(any(Authentication.class));
    }
    
    @Test
    public void testSignInWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("test@example.com")
                                                                                    .password("encodedPassword")
                                                                                    .authorities(new SimpleGrantedAuthority("USER"))
                                                                                    .build();
        
        when(customerUserDetailsService.loadUserByUsername(any(String.class))).thenReturn(userDetails);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);
        
        assertThrows(BadCredentialsException.class, () -> {
            authController.signIn(loginRequest);
        });
    }
}
