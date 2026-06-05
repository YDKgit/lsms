package com.example.lsms.lab.repository;

import com.example.lsms.lab.domain.SafetyEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyEquipmentRepository extends JpaRepository<SafetyEquipment, Long> {

    List<SafetyEquipment> findByLab_LabId(Long labId);
}
