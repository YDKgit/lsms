package com.example.lsms.lab.repository;

import com.example.lsms.lab.domain.FloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FloorPlanRepository extends JpaRepository<FloorPlan, Long> {

    Optional<FloorPlan> findFirstByLab_LabIdOrderByPlanIdDesc(Long labId);
}
