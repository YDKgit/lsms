package com.example.lsms.education.service;

import com.example.lsms.education.domain.EduCategory;
import com.example.lsms.education.domain.EduContent;
import com.example.lsms.education.domain.EduTerm;
import com.example.lsms.education.domain.LearningProgress;
import com.example.lsms.education.dto.EduContentRequestDTO;
import com.example.lsms.education.dto.EduContentResponseDTO;
import com.example.lsms.education.repository.EduContentRepository;
import com.example.lsms.education.repository.LearningProgressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final EduContentRepository eduContentRepository;
    private final LearningProgressRepository learningProgressRepository;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public String registerContent(EduContentRequestDTO dto) {
        EduCategory category = em.find(EduCategory.class, dto.getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("존재하지 않는 교육 카테고리입니다.");
        }

        EduTerm term = em.find(EduTerm.class, dto.getTermId());
        if (term == null) {
            throw new IllegalArgumentException("존재하지 않는 교육 학기/분기입니다.");
        }

        EduContent content = new EduContent();
        content.setTitle(dto.getTitle());
        content.setVideoUrl(dto.getVideoUrl());
        content.setDescription(dto.getDescription());
        content.setRequiredTime(dto.getRequiredTime());

        content.setEduCategory(category);
        content.setEduTerm(term);

        eduContentRepository.save(content);
        return "콘텐츠 등록이 완료되었습니다.";
    }

    public EduContentResponseDTO getLearningProgressDetail(String userId, Long contentId) {
        EduContent content = eduContentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교육 콘텐츠입니다."));

        LearningProgress progress = learningProgressRepository.findByUserIdAndEduContentId(userId, contentId)
                .orElseGet(() -> {
                    LearningProgress newProgress = new LearningProgress();
                    newProgress.setUserId(userId);
                    newProgress.setEduContent(content);
                    newProgress.setLastViewedPoint(0);
                    newProgress.setLearningRate(0);
                    newProgress.setCompleted(false);
                    return learningProgressRepository.save(newProgress);
                });

        return EduContentResponseDTO.builder()
                .contentId(content.getId())
                .title(content.getTitle())
                .videoUrl(content.getVideoUrl())
                .description(content.getDescription())
                .requiredTime(content.getRequiredTime())
                .categoryName(content.getEduCategory().getName())
                .termTitle(content.getEduTerm().getTitle())
                .lastViewedPoint(progress.getLastViewedPoint())
                .learningRate(progress.getLearningRate())
                .isCompleted(progress.isCompleted())
                .build();
    }

    @Transactional
    public String updateVideoProgress(String userId, Long contentId, int lastViewedPoint) {
        LearningProgress progress = learningProgressRepository.findByUserIdAndEduContentId(userId, contentId)
                .orElseThrow(() -> new IllegalArgumentException("진도 기록을 찾을 수 없습니다. 수강을 먼저 시작해주세요."));
        progress.updateProgressPoint(lastViewedPoint);

        return "success";
    }
}