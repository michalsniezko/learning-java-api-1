package com.example.locations.controller;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LocationControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user")
    void shouldCreateAndGetLocation() throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("country_code", "PL");
        jsonMap.put("party_id", "ABC");
        jsonMap.put("publish", true);

        String json = objectMapper.writeValueAsString(jsonMap);

        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.country_code").value("PL"));

        Location savedLocation = locationRepository.findAll().getFirst();

        mockMvc.perform(get("/api/locations/{id}", savedLocation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedLocation.getId().toString()))
                .andExpect(jsonPath("$.country_code").value("PL"));
    }

    @Test
    @WithMockUser(username = "user")
    void shouldReturn404WhenLocationNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/api/locations/{id}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user")
    void shouldDeleteLocation() throws Exception {
        Location location = new Location();
        location.setCountryCode("GB");
        location.setPartyId("ABC");
        location.setPublish(true);

        location = locationRepository.save(location);

        mockMvc.perform(delete("/api/locations/{id}", location.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/locations/{id}", location.getId()))
                .andExpect(status().isNotFound());
    }
}
