package com.example.locations.model;

import com.example.locations.validation.ValidCountryCode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Location {
    @Id
    private UUID uuid = UUID.randomUUID();

    @ValidCountryCode
    private String countryCode;

    @Length(max = 3, min = 3)
    @NotEmpty
    private String partyId;

    @Immutable
    @NotEmpty
    @Length(max = 36)
    private String id;

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
