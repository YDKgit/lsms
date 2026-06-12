package com.example.lsms.chemical.domain;

import com.example.lsms.lab.domain.LabInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "chemicals",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chemical_cas_number", columnNames = "cas_number"),
                @UniqueConstraint(name = "uk_chemical_cat_number", columnNames = "cat_number")
        }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Chemical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chemical_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private LabInfo lab;

    @NotBlank
    @Column(name = "cas_number", nullable = false, length = 50)
    private String casNumber;

    @NotBlank
    @Column(name = "cat_number", nullable = false, length = 50)
    private String catNumber;

    @NotBlank
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    @NotNull
    @PositiveOrZero
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Valid
    @NotNull
    @Embedded
    private CapacityUnit capacityUnit;

    @Valid
    @NotNull
    @Embedded
    private HazardInfo hazardInfo;

    @Valid
    @NotNull
    @Embedded
    private ChemicalProperty chemicalProperty;
}
