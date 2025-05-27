package com.example.locations.controller;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void cleanDb() {
        locationRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetLocation() throws Exception {
        String locationJson = "{\"name\":\"Integration Location\"}";

        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Integration Location"));

        Location savedLocation = locationRepository.findAll().getFirst();

        mockMvc.perform(get("/api/locations/{id}", savedLocation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedLocation.getId().toString()))
                .andExpect(jsonPath("$.name").value("Integration Location"));
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
        location = locationRepository.save(location);

        mockMvc.perform(delete("/api/locations/{id}", location.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/locations/{id}", location.getId()))
                .andExpect(status().isNotFound());
    }
}
