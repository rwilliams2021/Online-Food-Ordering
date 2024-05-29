package com.richard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {
    
    private final String jwtSecretKey;
    
    public AppConfig(@Value("${jwt.secret.key}") String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }
    /**
     * Configures the security filter chain for the application.
     *
     * @param  http  the HttpSecurity object to configure
     * @return       the SecurityFilterChain object
     * @throws Exception  if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                                                        .requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
                                                        .requestMatchers("/api/**").authenticated()
                                                        .anyRequest().permitAll()
                                  )
            .addFilterBefore(new JwtTokenValidator(jwtSecretKey), BasicAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        return http.build();
    }

    
    
    
    /**
     * Returns a CorsConfiguration object that specifies the allowed origins, methods, headers, credentials, and max age for CORS
     * requests.
     *
     * @return a CorsConfiguration object with the specified configuration
     */
    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(Arrays.asList(
                    "http://localhost:4200",
                    "http://localhost:8080"
                                               ));
            cfg.setAllowedMethods(Collections.singletonList("*"));
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            cfg.setExposedHeaders(Collections.singletonList("Authorization"));
            cfg.setMaxAge(3600L);
            return null;
        };
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
