package com.example.lsms.inspection.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checklist_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private boolean isUse = true;

    @Builder
    public Checklist(String category, String content, boolean isUse) {
        this.category = category;
        this.content = content;
        this.isUse = isUse;
    }

    public void updateItem(String category, String content) {
        this.category = category;
        this.content = content;
    }

    public void deactivate() {
        this.isUse = false;
    }
}