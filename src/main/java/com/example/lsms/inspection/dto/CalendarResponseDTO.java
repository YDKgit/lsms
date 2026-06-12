package com.example.lsms.inspection.dto;

import com.example.lsms.inspection.domain.Inspection;
import com.example.lsms.inspection.enums.InspectionType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalendarResponseDTO {
    private final Long inspectionId;
    private final LocalDate inspectionDate;
    private final InspectionType inspectionType;
    private final int defectCount;
    private final boolean isPass;

    public CalendarResponseDTO(Inspection inspection) {
        this.inspectionId = inspection.getInspectionId();
        this.inspectionDate = inspection.getInspectionDate();
        this.inspectionType = inspection.getInspectionType();
        this.defectCount = inspection.getDetailList().size();
        this.isPass = this.defectCount == 0;
    }
}
