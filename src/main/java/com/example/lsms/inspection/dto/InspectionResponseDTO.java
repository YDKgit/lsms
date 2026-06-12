package com.example.lsms.inspection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class InspectionResponseDTO {
    private Long inspectionID;
    private String labName;
    private String inspectorName;
    private LocalDate inspectionDate;

    @Builder
    public InspectionResponseDTO(Long inspectionID, String labName, String inspectorName, LocalDate inspectionDate) {
        this.inspectionID = inspectionID;
        this.labName = labName;
        this.inspectorName = inspectorName;
        this.inspectionDate = inspectionDate;
    }
}
