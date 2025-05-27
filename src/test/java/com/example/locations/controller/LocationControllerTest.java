package com.example.locations.controller;

import com.example.locations.model.Location;
import com.example.locations.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllLocations() throws Exception {
        Location loc1 = new Location();
        loc1.setId("id1");
        loc1.setUuid(UUID.randomUUID());
        Location loc2 = new Location();
        loc2.setId("id2");
        loc2.setUuid(UUID.randomUUID());

        when(locationService.getAll()).thenReturn(List.of(loc1, loc2));

        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].uuid").value(loc1.getUuid().toString()))
                .andExpect(jsonPath("$[0].id").value("id1"))
                .andExpect(jsonPath("$[1].id").value("id2"));
    }

    @Test
    void shouldReturnLocationById() throws Exception {
        UUID id = UUID.randomUUID();
        Location loc = new Location();
        loc.setUuid(id);
        loc.setId("id1");

        when(locationService.getById(id)).thenReturn(loc);

        mockMvc.perform(get("/api/locations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(id.toString()))
                .andExpect(jsonPath("$.id").value("id1"));
    }

    @Test
    void shouldCreateLocation() throws Exception {
        Location loc = new Location();
        loc.setUuid(UUID.randomUUID());
        loc.setId("test ID");
        loc.setPartyId("ABC");
        loc.setCountryCode("PL");

        when(locationService.create(any(Location.class))).thenReturn(loc);

        mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(loc.getUuid().toString()))
                .andExpect(jsonPath("$.id").value(loc.getId()));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(locationService).delete(id);

        mockMvc.perform(delete("/api/locations/{id}", id))
                .andExpect(status().isOk());

        verify(locationService).delete(eq(id));
    }

    @Test
    void getByIdShouldReturn404whenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(locationService.getById(id)).thenThrow(new EntityNotFoundException("Location not found with id: " + id));

        mockMvc.perform(get("/api/locations/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Location not found with id: " + id));
    }
}