package com.richard.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtTokenValidatorTest {
    
    private static final String SECRET_KEY = "your_jwt_secret_key_which_should_be_long_enough"; // Replace with your secret key
    private static final String VALID_JWT = generateJwtToken(false);
    private static final String EXPIRED_JWT = generateJwtToken(true);
    private static final String MALFORMED_JWT = "malformed.jwt.token";
    
    private JwtTokenValidator jwtTokenValidator;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenValidator = new JwtTokenValidator(SECRET_KEY);
        SecurityContextHolder.clearContext();
    }
    
    private static String generateJwtToken(boolean expired) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date issuedAt = new Date(expired ? System.currentTimeMillis() - 7200000 : System.currentTimeMillis());
        Date expiration = new Date(expired ? System.currentTimeMillis() - 3600000 : System.currentTimeMillis() + 3600000);
        
        return Jwts.builder()
                   .setSubject("user@example.com")
                   .claim("email", "user@example.com")
                   .claim("authorities", "ROLE_USER")
                   .setIssuedAt(issuedAt)
                   .setExpiration(expiration)
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
    }
    
    private MockHttpServletRequest createRequest(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (token != null) {
            request.addHeader(JwtConstant.JWT_HEADER, JwtConstant.JWT_PREFIX + token);
        }
        return request;
    }
    
    private void performFilter(MockHttpServletRequest request, MockHttpServletResponse response) throws ServletException, IOException {
        FilterChain filterChain = new MockFilterChain();
        jwtTokenValidator.doFilterInternal(request, response, filterChain);
    }
    
    @Test
    public void testValidJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = createRequest(VALID_JWT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        performFilter(request, response);
        
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertEquals("user@example.com", SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    @Test
    public void testExpiredJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = createRequest(EXPIRED_JWT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        performFilter(request, response);
        
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testMalformedJwtToken() throws ServletException, IOException {
        MockHttpServletRequest request = createRequest(MALFORMED_JWT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        performFilter(request, response);
        
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testMissingJwtPrefix() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JwtConstant.JWT_HEADER, VALID_JWT); // Missing "Bearer " prefix
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        performFilter(request, response);
        
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void testNoJwtHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        performFilter(request, response);
        
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
