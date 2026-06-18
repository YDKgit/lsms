package com.example.lsms.inspection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChecklistRequestDTO {

    @NotBlank(message = "점검 분야(카테고리)는 필수 입력 사항입니다.")
    private String category;

    @NotBlank(message = "점검 문항 내용은 필수 입력 사항입니다.")
    private String content;

}
