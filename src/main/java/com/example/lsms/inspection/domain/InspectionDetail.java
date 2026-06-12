package com.example.lsms.inspection.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_detail")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InspectionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id", nullable = false)
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    private Inspection inspection;

    // 불량이 발생한 점검 항목의 이름이나 카테고리 (예: "소화기 상태", "일반 지적사항")
    @Column(name = "issue_category", length = 100, nullable = false)
    private String issueCategory;

    @Column(name = "problem_describe", nullable = false, columnDefinition = "TEXT")
    private String problemDescribe;

    // 온라인 점검 시 특정 불량 항목에 대해 업로드하는 사진 경로
    @Column(name = "attached_file", length = 255)
    private String attachedFile;

    @Column(name = "action_result", length = 255)
    private String actionResult;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    protected void assignInspection(Inspection inspection) {
        this.inspection = inspection;
    }

}