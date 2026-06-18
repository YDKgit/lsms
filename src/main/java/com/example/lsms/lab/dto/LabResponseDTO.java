package com.example.lsms.lab.dto;

import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.SafetyEquipment;

import java.util.List;

public record LabResponseDTO(
        Long labId,
        String labName,
        String deptName,
        String managerName,
        String managerDepartment,
        String buildingLocation,
        String labType,
        String isInspectionTarget,
        String contact,
        String grade,
        String floorPlanUrl,
        String layoutUrl,
        String layoutData,
        List<Equip> equipDetails
) {
    public record Created(Long labId) {
    }

    public record Equip(
            Long equipId,
            String equipName,
            Integer quantity,
            String category,
            String status,
            String installedLocation
    ) {
        public static Equip from(SafetyEquipment equipment) {
            return new Equip(
                    equipment.getEquipId(),
                    equipment.getEquipName(),
                    equipment.getQuantity(),
                    equipment.getCategory(),
                    equipment.getStatus(),
                    null
            );
        }
    }

    public record Summary(Long labId, String labName, String buildingLocation, String labType) {
        public static Summary from(LabInfo lab) {
            return new Summary(
                    lab.getLabId(),
                    lab.getLabName(),
                    lab.getLocation(),
                    lab.getLabType()
            );
        }
    }

    public record FormOptions(
            List<String> labTypes,
            List<String> grades,
            List<String> inspectionTargetOptions
    ) {
    }

    public record FloorPlanUploaded(String filePath) {
    }

    public record LayoutPlanUploaded(String filePath) {
    }

    public record Dashboard(
            Long labId,
            String labName,
            String location,
            String grade,
            String labType
    ) {
        public static Dashboard from(LabInfo lab) {
            return new Dashboard(
                    lab.getLabId(),
                    lab.getLabName(),
                    lab.getLocation(),
                    lab.getGrade(),
                    lab.getLabType()
            );
        }
    }
}
