package com.richard.controller;

import com.richard.config.JwtProvider;
import com.richard.model.Cart;
import com.richard.model.UserRole;
import com.richard.model.User;
import com.richard.request.LoginRequest;
import com.richard.response.AuthResponse;
import com.richard.respository.CartRepository;
import com.richard.respository.UserRepository;
import com.richard.service.CustomerUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final CartRepository cartRepository;
    
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          CustomerUserDetailsService customerUserDetailsService,
                          CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customerUserDetailsService = customerUserDetailsService;
        this.cartRepository = cartRepository;
    }
    
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email is already used by another account.");
        }
        
        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullname(user.getFullname());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userRepository.save(createdUser);
        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtProvider.generateToken(authentication);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully.");
        authResponse.setRole(savedUser.getRole());
        
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req) throws Exception {
        
        String username = req.getEmail();
        String password = req.getPassword();
        
        Authentication authentication = authenticate(username, password);
        
        String jwt = jwtProvider.generateToken(authentication);
        
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User logged in successfully.");
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role =  authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        authResponse.setRole(UserRole.valueOf(role));
        
        return ResponseEntity.ok(authResponse);
    }
    
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username.");
        }
        
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password.");
        }
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

