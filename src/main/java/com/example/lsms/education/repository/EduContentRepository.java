package com.example.lsms.education.repository;

import com.example.lsms.education.domain.EduContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduContentRepository extends JpaRepository<EduContent, Long> {
}