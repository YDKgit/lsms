package com.example.lsms.waste.dto;

import com.example.lsms.waste.domain.WasteInfo;
import com.example.lsms.waste.domain.WasteStatus;

import java.time.LocalDateTime;

public record WasteResponse(
        Long id,
        String wasteName,
        String wasteTypeCode,
        String wasteTypeName,
        Long generatedLabId,
        String generatedLabName,
        String storageLocation,
        Long registeredById,
        String registeredByName,
        LocalDateTime registeredAt,
        WasteStatus status
) {
    public static WasteResponse from(WasteInfo wasteInfo) {
        return new WasteResponse(
                wasteInfo.getId(),
                wasteInfo.getWasteName(),
                wasteInfo.getWasteType().getCode(),
                wasteInfo.getWasteType().getName(),
                wasteInfo.getGeneratedLab().getLabId(),
                wasteInfo.getGeneratedLab().getLabName(),
                wasteInfo.getStorageLocation(),
                wasteInfo.getRegisteredBy().getId(),
                wasteInfo.getRegisteredBy().getName(),
                wasteInfo.getRegisteredAt(),
                wasteInfo.getStatus()
        );
    }
}
