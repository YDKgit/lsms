package com.example.lsms.lab.dto;

import com.example.lsms.lab.domain.LabInfo;

public record LabResponse(
        Long labId,
        String labName,
        String location,
        String labType,
        Long managerId,
        String managerName
) {
    public static LabResponse from(LabInfo lab) {
        return new LabResponse(
                lab.getLabId(),
                lab.getLabName(),
                lab.getLocation(),
                lab.getLabType(),
                lab.getManager() == null ? null : lab.getManager().getId(),
                lab.getManager() == null ? null : lab.getManager().getName()
        );
    }
}
