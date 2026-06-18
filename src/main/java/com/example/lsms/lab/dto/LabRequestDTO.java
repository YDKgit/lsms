package com.example.lsms.lab.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LabRequestDTO(
        @NotBlank String labName,
        @NotNull Long managerId,
        @NotBlank String buildingLocation,
        String labType,
        String isInspectionTarget,
        String contact,
        String grade,
        String signImagePath,
        String photoImagePath,
        @Valid List<Equip> equipList,
        List<Long> memberUserIds,
        FloorPlan floorPlan,
        Layout layout
) {
    public MasterCreate toMasterCreate() {
        return new MasterCreate(
                labName, managerId, buildingLocation,
                labType, isInspectionTarget, contact, grade,
                signImagePath, photoImagePath, memberUserIds
        );
    }

    public record MasterCreate(
            @NotBlank String labName,
            @NotNull Long managerId,
            @NotBlank String buildingLocation,
            String labType,
            String isInspectionTarget,
            String contact,
            String grade,
            String signImagePath,
            String photoImagePath,
            List<Long> memberUserIds
    ) {
    }

    public record Equip(
            @NotBlank String equipName,
            Integer quantity,
            String category,
            String status,
            String installedLocation
    ) {
    }

    public record FloorPlan(
            String buildingName,
            Integer floorLevel,
            String filePath
    ) {
    }

    public record Layout(
            String filePath,
            String layoutData
    ) {
    }

    public record LayoutSave(
            String layoutData,
            String filePath
    ) {
    }
}
