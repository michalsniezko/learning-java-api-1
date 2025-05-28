package com.example.locations.bootstrap;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@Profile({"dev", "test"})
public class LocationLoader implements CommandLineRunner {
    private final LocationRepository locationRepository;
    private final Faker faker = new Faker();

    public LocationLoader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            locationRepository.save(generateLocation());
        }
    }

    private Location generateLocation() {
        Location loc = new Location();
        loc.setUuid(UUID.randomUUID());
        loc.setCountryCode(faker.country().countryCode2().toUpperCase());
        loc.setId(faker.lorem().word());
        loc.setPartyId(faker.letterify("???").toUpperCase());
        loc.setPublish(new Random().nextBoolean());
        return loc;
    }
}
