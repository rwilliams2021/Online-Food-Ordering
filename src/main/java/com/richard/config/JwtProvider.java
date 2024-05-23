package com.richard.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());
    
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
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        
        return String.valueOf(claims.get("email"));
    }
    
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        
        authorities.forEach(authority -> auths.add(authority.getAuthority()));
        
        return String.join(",", auths);
    }
}
