package com.example.lsms.inspection.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class InspectionDetailResponseDTO {
    private Long detailId;
    private String issueCategory;
    private String problemDescribe;
    private String attachedFile;
    private String actionResult;
    private LocalDateTime actionDate;

    @Builder
    public InspectionDetailResponseDTO(Long detailId, String issueCategory, String problemDescribe, String attachedFile, String actionResult, LocalDateTime actionDate) {
        this.detailId = detailId;
        this.issueCategory = issueCategory;
        this.problemDescribe = problemDescribe;
        this.attachedFile = attachedFile;
        this.actionResult = actionResult;
        this.actionDate = actionDate;
    }
}
