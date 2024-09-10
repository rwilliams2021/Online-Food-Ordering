package com.richard.controller.restaurant;

import com.richard.config.AppConfig;
import com.richard.config.JwtConstant;
import com.richard.config.JwtProvider;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminRestaurantController.class)
@Import({ AppConfig.class, JwtProvider.class })
public class AdminRestaurantControllerTest {
    
    private static final String CREATE_RESTAURANT_JSON = "{\"name\":\"Test Restaurant\"}";
    private static final String UPDATE_RESTAURANT_JSON = "{\"name\":\"Updated Restaurant\"}";
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
    @MockBean
    private RestaurantService restaurantService;
    
    @MockBean
    private UserService userService;
    
    @Mock
    private Authentication authentication;
    
    @Autowired
    private JwtProvider jwtProvider;
    
    private String bearerMockToken;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        
        // Mock the Authentication object with required authorities
        when(authentication.getName()).thenReturn("customer2@gmail.com");
        when(authentication.getAuthorities()).thenReturn((Collection)(List.of(new SimpleGrantedAuthority("CUSTOMER"))));
        
        // Generate the JWT token dynamically using the real JwtProvider
        bearerMockToken = jwtProvider.generateToken(authentication);
    }
    
    @ParameterizedTest
    @CsvSource({ "POST, /admin/restaurant, " + CREATE_RESTAURANT_JSON,
                 "PUT, /admin/restaurant/1, " + UPDATE_RESTAURANT_JSON,
                 "DELETE, /admin/restaurant/1, ",
                 "PUT, /admin/restaurant/1/status, ",
                 "GET, /admin/restaurant/user, ",
                 "GET, /admin/restaurant, " })
    @WithMockUser(roles = "CUSTOMER")
    public void testRequestsForbiddenForCustomer(String method, String url, String content) throws Exception {
        mockMvc.perform(request(HttpMethod.valueOf(method), url).header(JwtConstant.JWT_HEADER,
                                                                        JwtConstant.JWT_PREFIX + bearerMockToken)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(content != null ? content : ""))
               .andExpect(status().isForbidden());
    }
}
