package com.example.lsms.inspection.dto;

import com.example.lsms.inspection.domain.Checklist;
import lombok.Getter;

@Getter
public class ChecklistResponseDTO {
    private final Long id;
    private final String category;
    private final String content;
    private final boolean isUse;

    public ChecklistResponseDTO(Checklist entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.content = entity.getContent();
        this.isUse = entity.isUse();
    }
}
