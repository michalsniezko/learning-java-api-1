package com.example.locations.controller;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        locationRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetLocation() throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "Test Location");
        jsonMap.put("country-code", "PL");

        String json = objectMapper.writeValueAsString(jsonMap);

        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Location"));

        Location savedLocation = locationRepository.findAll().getFirst();

        mockMvc.perform(get("/api/locations/{id}", savedLocation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedLocation.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test Location"));
    }

    @Test
    void shouldReturn404WhenLocationNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/api/locations/{id}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        Location location = new Location();
        location.setName("ToDelete");
        location.setCountryCode("GB");
        location = locationRepository.save(location);

        mockMvc.perform(delete("/api/locations/{id}", location.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/locations/{id}", location.getId()))
                .andExpect(status().isNotFound());
    }
}
