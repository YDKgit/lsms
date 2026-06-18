package com.example.lsms.lab.domain;

import com.example.lsms.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lab_info")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LabInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_id", nullable = false)
    private Long labId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "lab_name", nullable = false, length = 100)
    private String labName;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "lab_type", length = 20)
    private String labType;

    @Column(name = "is_inspection_target", columnDefinition = "CHAR(1)")
    private String isInspectionTarget;

    @Column(name = "contact", length = 50)
    private String contact;

    @Column(name = "grade", length = 20)
    private String grade;

    @Column(name = "sign_image_path", length = 255)
    private String signImagePath;

    @Column(name = "photo_image_path", length = 255)
    private String photoImagePath;
}

