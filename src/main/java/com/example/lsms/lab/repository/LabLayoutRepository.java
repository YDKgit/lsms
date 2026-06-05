package com.example.lsms.lab.repository;

import com.example.lsms.lab.domain.LabLayout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabLayoutRepository extends JpaRepository<LabLayout, Long> {

    Optional<LabLayout> findFirstByLab_LabIdOrderByLayoutIdDesc(Long labId);
}
