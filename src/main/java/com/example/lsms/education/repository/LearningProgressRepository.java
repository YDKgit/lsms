package com.example.lsms.education.repository;

import com.example.lsms.education.domain.LearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    List<LearningProgress> findByUserIdAndEduContent_Id(Long userId, Long contentId);
}