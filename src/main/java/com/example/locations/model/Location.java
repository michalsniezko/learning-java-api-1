package com.example.locations.model;

import com.example.locations.validation.ValidCountryCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Location {
    @Id
    private UUID id = UUID.randomUUID();

    @JsonProperty("country-code")
    @ValidCountryCode
    private String countryCode;

    private String name;
}
