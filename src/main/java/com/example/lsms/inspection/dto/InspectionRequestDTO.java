package com.example.lsms.inspection.dto;

import com.example.lsms.inspection.enums.InspectionMethod;
import com.example.lsms.inspection.enums.InspectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점검 등록 요청 DTO")
public class InspectionRequestDTO {

    @Schema(description = "연구실 ID", example = "1")
    private Long labID;

    @Schema(description = "점검자 ID", example = "2")
    private Long inspectorID;

    @Schema(description = "점검 일자")
    private LocalDate inspectionDate;

    @Schema(description = "점검 방식", example = "ONLINE")
    private InspectionMethod inspectionMethod;

    @Schema(description = "점검 등급", example = "5.0")
    @NotNull(message = "점검 등급은 필수입니다.")
    @DecimalMin(value = "1.0", message = "점검 등급은 최소 1점 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "점검 등급은 최대 5점 이하여야 합니다.")
    private Double inspectionGrade;

    @Schema(description = "점검 유형 (DAILY, REGULAR 등)")
    private InspectionType inspectionType;

    @Schema(description = "오프라인 점검표 스캔본")
    private MultipartFile scanFile;

    @Schema(description = "점검 상세 결과(지적 사항) 리스트")
    private List<InspectionDetailRequestDTO> detailList;
}