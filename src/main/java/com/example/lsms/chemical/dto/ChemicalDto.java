package com.example.lsms.chemical.dto;

import com.example.lsms.chemical.domain.CapacityUnit;
import com.example.lsms.chemical.domain.Chemical;
import com.example.lsms.chemical.domain.ChemicalProperty;
import com.example.lsms.chemical.domain.HazardInfo;
import com.example.lsms.lab.domain.LabInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ChemicalDto {

    public record RegisterRequest(
            @NotNull @Positive Long labId,
            @NotBlank String casNumber,
            @NotBlank String catNumber,
            @NotBlank String name,
            String manufacturer,
            @NotNull @PositiveOrZero Double amount,
            @Valid @NotNull CapacityUnit capacityUnit,
            @Valid @NotNull HazardInfo hazardInfo,
            @Valid @NotNull ChemicalProperty chemicalProperty
    ) {

        public Chemical toEntity(LabInfo lab) {
            return Chemical.builder()
                    .lab(lab)
                    .casNumber(casNumber)
                    .catNumber(catNumber)
                    .name(name)
                    .manufacturer(manufacturer)
                    .amount(amount)
                    .capacityUnit(capacityUnit)
                    .hazardInfo(hazardInfo)
                    .chemicalProperty(chemicalProperty)
                    .build();
        }
    }

    public record Response(
            Long id,
            Long labId,
            String casNumber,
            String catNumber,
            String name,
            String manufacturer,
            Double amount,
            CapacityUnit capacityUnit,
            HazardInfo hazardInfo,
            ChemicalProperty chemicalProperty
    ) {

        public static Response from(Chemical chemical) {
            Long labId = chemical.getLab() == null ? null : chemical.getLab().getLabId();
            return new Response(
                    chemical.getId(),
                    labId,
                    chemical.getCasNumber(),
                    chemical.getCatNumber(),
                    chemical.getName(),
                    chemical.getManufacturer(),
                    chemical.getAmount(),
                    chemical.getCapacityUnit(),
                    chemical.getHazardInfo(),
                    chemical.getChemicalProperty()
            );
        }
    }
}
