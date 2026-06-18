package com.example.lsms.education.repository;

import com.example.lsms.education.domain.EduCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduCategoryRepository extends JpaRepository<EduCategory, Long> {
}
