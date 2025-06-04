package com.example.locations.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


public class RestTemplateClient {
    private final static String GET_ALL_LOCATIONS_API = "http://localhost:8080/api/locations";
    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws JsonProcessingException {
        String token = getJwtToken();
        callGetAllLocationsAPI(token);
    }

    private static void callGetAllLocationsAPI(String jwtToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(jwtToken);

        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        ResponseEntity<String> result = restTemplate.exchange(GET_ALL_LOCATIONS_API, HttpMethod.GET, entity, String.class);
        System.out.println(result);
    }

    private static String getJwtToken() throws JsonProcessingException {
        String loginUrl = "http://localhost:8080/sign-in";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> credentials = Map.of(
                "username", "user",
                "password", "user123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);

        assert response.getBody() != null;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        return jsonNode.get("jwt_token").asText();
    }
}