package com.example.lsms.waste.domain;

import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "waste_info")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WasteInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waste_info_id", nullable = false)
    private Long id;

    @Column(name = "waste_name", nullable = false, length = 100)
    private String wasteName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_type", nullable = false)
    private WasteType wasteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_lab_id", nullable = false)
    private LabInfo generatedLab;

    @Column(name = "storage_location", nullable = false, length = 100)
    private String storageLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by", nullable = false)
    private User registeredBy;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private WasteStatus status;

    public void update(String wasteName, WasteType wasteType, LabInfo generatedLab,
                       String storageLocation, WasteStatus status) {
        this.wasteName = wasteName;
        this.wasteType = wasteType;
        this.generatedLab = generatedLab;
        this.storageLocation = storageLocation;
        this.status = status;
    }

    public void markDeleted() {
        this.status = WasteStatus.DELETED;
    }
}
