package com.example.lsms.education.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "learning_progresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    private int lastViewedPoint;

    private int learningRate; // 누적 진도율 (0 ~ 100)

    @Column(nullable = false)
    private boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private EduContent eduContent;

    public void updateProgressPoint(int currentPoint) {
        this.lastViewedPoint = currentPoint;

        int requiredTime = this.eduContent.getRequiredTime();
        this.learningRate = Math.min(100, (int) ((double) currentPoint / requiredTime * 100));

        if (this.learningRate >= 100) {
            this.isCompleted = true;
        }
    }
}