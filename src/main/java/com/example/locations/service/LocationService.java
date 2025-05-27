package com.example.locations.service;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {
    private final LocationRepository repo;

    public LocationService(LocationRepository repo) {
        this.repo = repo;
    }

    public List<Location> getAll() {
        return repo.findAll();
    }

    public Location getById(UUID id) {
        return repo.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }

    public Location create(Location location) {
        return repo.save(location);
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }
}
