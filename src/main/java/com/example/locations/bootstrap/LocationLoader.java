package com.example.locations.bootstrap;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("dev")
public class LocationLoader implements CommandLineRunner {
    private final LocationRepository locationRepository;
    private final Faker faker = new Faker();

    public LocationLoader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            Location loc = new Location();
            loc.setId(UUID.randomUUID());
            loc.setCountryCode(faker.country().countryCode2());
            loc.setName(faker.witcher().location());

            locationRepository.save(loc);
        }
    }
}
