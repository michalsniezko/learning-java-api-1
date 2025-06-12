package com.example.locations.security;

import com.example.locations.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String generateToken(UserDetails userDetails) {
        return jwtUtils.generateTokenFromUsername(userDetails);
    }

    @Test
    void accessProtectedEndpointWithoutTokenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpointWithInvalidTokenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/locations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessProtectedEndpointWithValidTokenShouldReturnOk() throws Exception {
        UserDetails userDetails = User.withUsername("user").password("{noop}test").roles("USER").build();

        String token = generateToken(userDetails);

        mockMvc.perform(get("/api/locations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }
}
