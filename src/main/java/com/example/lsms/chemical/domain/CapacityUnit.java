package com.example.lsms.chemical.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
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
public class CapacityUnit {

    @Column(name = "capacity_unit_id")
    private Integer id;

    @NotBlank
    @Column(name = "capacity_unit_name", length = 50)
    private String name;

    @NotBlank
    @Column(name = "capacity_unit_symbol", length = 20)
    private String symbol;
}
