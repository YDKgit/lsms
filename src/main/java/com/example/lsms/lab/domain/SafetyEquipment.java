package com.example.lsms.lab.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "safety_equipment")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SafetyEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equip_id", nullable = false)
    private Long equipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private LabInfo lab;

    @Column(name = "equip_name", nullable = false, length = 100)
    private String equipName;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status", length = 50)
    private String status;
}

