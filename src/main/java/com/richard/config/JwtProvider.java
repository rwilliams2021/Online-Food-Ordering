package com.richard.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {
    
    private final SecretKey key;
    
    public JwtProvider(@Value("${jwtSecretKey}") String jwtSecretKey) {
        this.key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }
    
    public String generateToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities(); //
        String roles = populateAuthorities(authorities);
        return Jwts.builder().setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 84000000))
                   .claim("email", auth.getName())
                   .claim("authorities", roles)
                   .signWith(key)
                   .compact();
    }
    
    public String getEmailFromJwtToken(String jwt) {
        try {
            // Remove the "Bearer " prefix if present
            if (jwt.startsWith(JwtConstant.JWT_PREFIX)) {
                jwt = jwt.substring(7).trim();
            }
            
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(jwt)
                                .getBody();
            
            return claims.get("email", String.class);
        } catch (JwtException e) {
            // Log the exception and handle it appropriately
            System.err.println("Invalid JWT token: " + e.getMessage());
            return null;
        }
    }

    
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        
        authorities.forEach(authority -> auths.add(authority.getAuthority()));
        
        return String.join(",", auths);
    }
}
