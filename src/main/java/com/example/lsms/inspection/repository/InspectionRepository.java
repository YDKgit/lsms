package com.example.lsms.inspection.repository;

import com.example.lsms.inspection.domain.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE InspectionDetail d " +
            "SET d.actionResult = :status, d.actionDate = CURRENT_TIMESTAMP " +
            "WHERE d.detailId = :detailId")
    int updateStatus(@Param("detailId") Long detailId, @Param("status") String status);

    @Query("SELECT i FROM Inspection i " +
            "LEFT JOIN FETCH i.detailList " +
            "WHERE i.lab.labId = :labId " +
            "AND i.inspectionDate BETWEEN :startDate AND :endDate " +
            "ORDER BY i.inspectionDate ASC")
    List<Inspection> findInspectionsByMonth(
            @Param("labId") Long labId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT i FROM Inspection i WHERE i.lab.labId IN " +
            "(SELECT m.lab.labId FROM LabUserMapping m WHERE m.user.id = :userId) " +
            "ORDER BY i.inspectionDate DESC")
    List<Inspection> findInspectionsByUserId(@Param("userId") Long userId);
}