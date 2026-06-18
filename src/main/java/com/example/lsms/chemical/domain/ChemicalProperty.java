package com.example.lsms.chemical.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChemicalProperty {

    @Column(name = "appearance", length = 200)
    private String appearance;

    @Column(name = "odor", length = 200)
    private String odor;

    @DecimalMin("0.0")
    @DecimalMax("14.0")
    @Column(name = "ph")
    private Double ph;

    @Column(name = "melting_point")
    private Double meltingPoint;

    @Column(name = "boiling_point")
    private Double boilingPoint;

    @Column(name = "flash_point")
    private Double flashPoint;

    @Column(name = "ignition_point")
    private Double ignitionPoint;
}
