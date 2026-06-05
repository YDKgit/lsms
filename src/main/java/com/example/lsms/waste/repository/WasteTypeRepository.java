package com.example.lsms.waste.repository;

import com.example.lsms.waste.domain.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteTypeRepository extends JpaRepository<WasteType, String> {
}
