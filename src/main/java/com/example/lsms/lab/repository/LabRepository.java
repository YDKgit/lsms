package com.example.lsms.lab.repository;

import com.example.lsms.lab.domain.LabInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabRepository extends JpaRepository<LabInfo, Long> {

    @Override
    LabInfo save(LabInfo entity);

    @Query("SELECT l FROM LabInfo l LEFT JOIN FETCH l.manager WHERE l.labId = :labId")
    Optional<LabInfo> findByLabId(@Param("labId") Long labId);

    @Query("SELECT l FROM LabInfo l ORDER BY l.labId")
    List<LabInfo> findAllLabs();

    boolean existsByLocation(String location);
}
