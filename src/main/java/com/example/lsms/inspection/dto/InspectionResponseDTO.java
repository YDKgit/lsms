package com.example.lsms.inspection.dto;

import com.example.lsms.inspection.enums.InspectionMethod;
import com.example.lsms.inspection.enums.InspectionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class InspectionResponseDTO {
    private Long inspectionID;
    private String labName;
    private String inspectorName;
    private LocalDate inspectionDate;
    private InspectionType inspectionType;
    private InspectionMethod inspectionMethod;
    private Double inspectionGrade;
    private LocalDateTime readDateTime;
    private List<InspectionDetailResponseDTO> detailList;

    @Builder
    public InspectionResponseDTO(
            Long inspectionID,
            String labName,
            String inspectorName,
            LocalDate inspectionDate,
            InspectionType inspectionType,
            InspectionMethod inspectionMethod,
            Double inspectionGrade,
            LocalDateTime readDateTime,
            List<InspectionDetailResponseDTO> detailList
    ) {
        this.inspectionID = inspectionID;
        this.labName = labName;
        this.inspectorName = inspectorName;
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.inspectionMethod = inspectionMethod;
        this.inspectionGrade = inspectionGrade;
        this.readDateTime = readDateTime;
        this.detailList = detailList;
    }
}
