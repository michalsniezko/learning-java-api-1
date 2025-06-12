package com.example.locations.bootstrap;

import com.example.locations.model.Location;
import com.example.locations.repository.LocationRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("dev")
@Transactional
public class LocationLoader implements CommandLineRunner {
    private final LocationRepository locationRepository;
    private final Faker faker = new Faker();

    public LocationLoader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void run(String... args) {
        if (locationRepository.count() > 0) return;

        for (int i = 0; i < 10; i++) {
            locationRepository.save(generateLocation());
        }
    }

    private Location generateLocation() {
        return Location.builder().
                countryCode(faker.country().countryCode2().toUpperCase()).
                partyId(faker.letterify("???").toUpperCase()).
                publish(new Random().nextBoolean())
                .build();
    }
}
