package com.example.lsms.waste.dto;

import com.example.lsms.waste.domain.WasteType;

public record WasteTypeResponse(String code, String name, String description) {
    public static WasteTypeResponse from(WasteType type) {
        return new WasteTypeResponse(type.getCode(), type.getName(), type.getDescription());
    }
}
