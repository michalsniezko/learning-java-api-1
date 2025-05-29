package com.example.locations.model;

import com.example.locations.validation.ValidCountryCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Location {
    @Id
    @GeneratedValue
    private UUID id;

    @ValidCountryCode
    private String countryCode;

    @Length(max = 3, min = 3)
    @NotEmpty
    private String partyId;

    @NotNull
    private Boolean publish;

    @PrePersist
    @PreUpdate
    private void formatCountryCode() {
        if (countryCode != null) {
            countryCode = countryCode.toUpperCase();
        } else {
            throw new IllegalStateException("Country code must not be null or blank");
        }
    }
}
