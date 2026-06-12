package com.example.lsms.inspection.repository;

import com.example.lsms.inspection.domain.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findByIsUseTrue();
}
