package com.example.locations.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LocationControllerRestTemplateTest {

    private final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void shouldGetAllLocationsWithJwtToken() throws JsonProcessingException {
        HttpEntity<Void> loggedInHttpEntity = getLoggedInHttpEntity();

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/api/locations",
                HttpMethod.GET,
                loggedInHttpEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private @NotNull HttpEntity<Void> getLoggedInHttpEntity() throws JsonProcessingException {
        String loginUrl = BASE_URL + "/sign-in";

        Map<String, String> credentials = Map.of(
                "username", "user",
                "password", "user123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> loginRequest = new HttpEntity<>(credentials, headers);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(loginResponse.getBody());
        String jwtToken = jsonNode.get("jwt_token").asText();

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(jwtToken);

        return new HttpEntity<>(authHeaders);
    }

}
