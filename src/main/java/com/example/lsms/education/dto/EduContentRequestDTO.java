package com.example.lsms.education.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EduContentRequestDTO {

    @NotBlank(message = "영상 제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "영상 URL은 필수 입력 항목입니다.")
    private String videoUrl;

    private String description;

    @NotNull(message = "필수 시청 시간은 필수 입력 항목입니다.")
    private Integer requiredTime;

    @NotNull(message = "카테고리 ID는 필수 항목입니다.")
    private Long categoryId;

    @NotNull(message = "교육 학기 ID는 필수 항목입니다.")
    private Long termId;
}