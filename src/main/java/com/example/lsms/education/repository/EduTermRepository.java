package com.example.lsms.education.repository;

import com.example.lsms.education.domain.EduTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduTermRepository extends JpaRepository<EduTerm, Long> {
}
