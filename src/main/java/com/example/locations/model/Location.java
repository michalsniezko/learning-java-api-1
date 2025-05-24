package com.example.locations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Location {
    @Id
    private UUID id = UUID.randomUUID();
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
}
