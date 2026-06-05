package com.example.lsms.waste.repository;

import com.example.lsms.waste.domain.WasteInfo;
import com.example.lsms.waste.domain.WasteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WasteInfoRepository extends JpaRepository<WasteInfo, Long> {

    @Query("""
            select w from WasteInfo w
            join fetch w.wasteType wt
            join fetch w.generatedLab l
            join fetch w.registeredBy u
            where w.id = :id and w.status <> :deletedStatus
            """)
    Optional<WasteInfo> findActiveById(@Param("id") Long id, @Param("deletedStatus") WasteStatus deletedStatus);

    @Query("""
            select w from WasteInfo w
            join fetch w.wasteType wt
            join fetch w.generatedLab l
            join fetch w.registeredBy u
            where w.status <> :deletedStatus
              and (:wasteName is null or lower(w.wasteName) like lower(concat('%', :wasteName, '%')))
              and (:wasteTypeCode is null or wt.code = :wasteTypeCode)
              and (:generatedLabId is null or l.labId = :generatedLabId)
              and (:storageLocation is null or lower(w.storageLocation) like lower(concat('%', :storageLocation, '%')))
              and (:status is null or w.status = :status)
            order by w.registeredAt desc, w.id desc
            """)
    List<WasteInfo> searchActive(
            @Param("wasteName") String wasteName,
            @Param("wasteTypeCode") String wasteTypeCode,
            @Param("generatedLabId") Long generatedLabId,
            @Param("storageLocation") String storageLocation,
            @Param("status") WasteStatus status,
            @Param("deletedStatus") WasteStatus deletedStatus
    );
}
