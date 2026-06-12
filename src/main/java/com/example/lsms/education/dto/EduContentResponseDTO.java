package com.example.lsms.education.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EduContentResponseDTO {

    private Long contentId;
    private String title;
    private String videoUrl;
    private String description;
    private int requiredTime;

    private String categoryName;
    private String termTitle;

    private int lastViewedPoint;
    private int learningRate;
    private boolean isCompleted;
}