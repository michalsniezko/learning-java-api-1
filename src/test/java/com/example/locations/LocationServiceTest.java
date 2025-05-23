package com.example.locations;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.example.locations.service.LocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository repository;

    @InjectMocks
    private LocationService service;

    @Test
    void testGetAll() {
        List<Location> list = List.of(new Location());
        when(repository.findAll()).thenReturn(list);

        List<Location> result = service.getAll();

        assertEquals(1, result.size());
    }
}
