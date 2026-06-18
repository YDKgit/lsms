package com.example.lsms.lab.repository;

import com.example.lsms.lab.domain.LabUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabUserMappingRepository extends JpaRepository<LabUserMapping, Long> {

    List<LabUserMapping> findByLab_LabId(Long labId);

    List<LabUserMapping> findByUser_Id(Long userId);
}
