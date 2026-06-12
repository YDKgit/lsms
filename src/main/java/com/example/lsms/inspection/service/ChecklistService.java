package com.example.lsms.inspection.service;

import com.example.lsms.inspection.domain.Checklist;
import com.example.lsms.inspection.dto.ChecklistRequestDTO;
import com.example.lsms.inspection.dto.ChecklistResponseDTO;
import com.example.lsms.inspection.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    @Transactional
    public ChecklistResponseDTO registerItem(ChecklistRequestDTO request) {
        Checklist newItem = Checklist.builder()
                .category(request.getCategory())
                .content(request.getContent())
                .isUse(true)
                .build();

        Checklist savedItem = checklistRepository.save(newItem);
        return new ChecklistResponseDTO(savedItem);
    }

    public List<ChecklistResponseDTO> getActiveChecklist() {
        return checklistRepository.findByIsUseTrue().stream()
                .map(ChecklistResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ChecklistResponseDTO> getAllChecklistItems() {
        return checklistRepository.findAll().stream()
                .map(ChecklistResponseDTO::new)
                .collect(Collectors.toList());
    }
}