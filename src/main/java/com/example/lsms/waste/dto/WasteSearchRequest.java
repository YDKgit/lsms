package com.example.lsms.waste.dto;

import com.example.lsms.waste.domain.WasteStatus;

public record WasteSearchRequest(
        String wasteName,
        String wasteTypeCode,
        Long generatedLabId,
        String storageLocation,
        WasteStatus status
) {
}
