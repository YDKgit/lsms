package com.example.lsms.waste.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "waste_type")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WasteType {

    @Id
    @Column(name = "waste_type_code", nullable = false, length = 30)
    private String code;

    @Column(name = "waste_type_name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;
}
