package com.example.lsms.inspection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점검 등록 시 지적 사항 DTO")
public class InspectionDetailRequestDTO {

    @NotBlank(message = "지적/분류 항목명은 필수입니다.")
    private String issueCategory;

    @Schema(description = "지적 사항", example = "소화기 기한 만료")
    @NotBlank(message = "문제점(지적 사항) 내용은 필수입니다.")
    private String problemDescribe;

    @Schema(description = "첨부파일")
    private MultipartFile attachedFile;
}