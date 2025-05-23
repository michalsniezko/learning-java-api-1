package com.example.locations.repository;

import com.example.locations.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findByNameContainingIgnoreCase(String name);
}
