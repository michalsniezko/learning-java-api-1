package com.example.locations.service;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationService service;

    @Test
    void getAll() {
        List<Location> list = List.of(new Location());
        when(repository.findAll()).thenReturn(list);

        List<Location> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getById() {
        UUID id = UUID.randomUUID();
        Location location = new Location();
        location.setId(id);
        location.setName("test");

        when(repository.findById(id)).thenReturn(Optional.of(location));

        Location result = service.getById(id);

        assertNotNull(result);
        assertEquals(location.getId(), result.getId());
        assertEquals("test", result.getName());
    }

    @Test
    void getByIdThrows() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(id));
    }

    @Test
    void create() {
        Location location = new Location();
        location.setName("Test Name");
        location.setAddress("Test Address");
        location.setCity("Test City");
        location.setLatitude(50.0);
        location.setLongitude(20.0);

        when(repository.save(location)).thenReturn(location);

        Location result = service.create(location);

        assertNotNull(result);
        assertEquals("Test Name", result.getName());
        assertEquals("Test Address", result.getAddress());
        assertEquals("Test City", result.getCity());
        verify(repository, times(1)).save(location);
    }

    @Test
    void shouldDeleteLocationById() {
        UUID id = UUID.randomUUID();
        service.delete(id);

        verify(repository).deleteById(id);
    }
}