package com.example.lsms.inspection.domain;

import com.example.lsms.inspection.enums.InspectionMethod;
import com.example.lsms.inspection.enums.InspectionType;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inspection")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_id", nullable = false)
    private Long inspectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private LabInfo lab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspector_id", nullable = false)
    private User inspector;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    // 일상/정기 점검 구분
    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_type", nullable = false)
    private InspectionType inspectionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_method", nullable = false)
    private InspectionMethod inspectionMethod;

    @Column(name = "inspection_grade")
    private Double inspectionGrade;

    // 오프라인 점검표 스캔본 저장 경로
    @Column(name = "attached_file_path")
    private String attachedFilePath;

    @Column(name = "read_date_time")
    private LocalDateTime readDateTime;

    @Builder.Default
    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionDetail> detailList = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addDetail(InspectionDetail detail) {
        this.detailList.add(detail);
        detail.assignInspection(this);
    }

    public void updateReadDateTime() {
        this.readDateTime = LocalDateTime.now();
    }
}