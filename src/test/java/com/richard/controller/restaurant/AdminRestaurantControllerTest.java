package com.richard.controller.restaurant;

import com.richard.config.AppConfig;
import com.richard.service.RestaurantService;
import com.richard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminRestaurantController.class)
@Import(AppConfig.class)
public class AdminRestaurantControllerTest {
    
    public static final String BEARER_MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
                                                   ".eyJpYXQiOjE3MjUzNjgzMjgsImV4cCI6MTcyNTQ1MjMyOCwiZW1haWwiOiJjdXN0b21lcjJAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJDVVNUT01FUiJ9.WLGH95xehi-WMOahAimI3wgerax8h4O0ZzRho6fCu0c";
    private static final String CREATE_RESTAURANT_JSON = "{\"name\":\"Test Restaurant\"}";
    private static final String UPDATE_RESTAURANT_JSON = "{\"name\":\"Updated Restaurant\"}";
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
    @MockBean
    private RestaurantService restaurantService;
    
    @MockBean
    private UserService userService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
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
        mockMvc.perform(request(HttpMethod.valueOf(method), url).header("Authorization", "Bearer " + BEARER_MOCK_TOKEN)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(content != null ? content : ""))
               .andExpect(status().isForbidden());
    }
}
