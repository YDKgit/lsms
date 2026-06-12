package com.example.lsms.inspection.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarRequestDTO {
    @Schema(description = "조회할 연도", example = "2026")
    @NotNull(message = "조회할 연도는 필수입니다.")
    private Integer year;

    @Schema(description = "조회할 월 (1~12)", example = "5")
    @NotNull(message = "조회할 월은 필수입니다.")
    @Min(value = 1, message = "월은 1 이상이어야 합니다.")
    @Max(value = 12, message = "월은 12 이하여야 합니다.")
    private Integer month;

    @Schema(description = "특정 연구실 ID (선택 사항)", example = "1")
    private Long labId;
}
