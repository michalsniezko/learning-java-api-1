package com.example.locations.controller;

import com.example.locations.model.Location;
import com.example.locations.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@PreAuthorize("hasRole('USER')")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(produces = "application/json")
    public List<Location> getAll() {
        return locationService.getAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Location getById(@PathVariable UUID id) {
        return locationService.getById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Location create(@Valid @RequestBody Location location) {
        return locationService.create(location);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        locationService.delete(id);
    }
}
