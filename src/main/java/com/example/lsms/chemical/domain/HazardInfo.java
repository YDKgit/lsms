package com.example.lsms.chemical.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HazardInfo {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "signal_word", nullable = false, length = 20)
    private SignalWord signalWord;

    @Column(name = "hazard_statement", length = 1000)
    private String hazardStatement;

    @Column(name = "precautionary_statement", length = 1000)
    private String precautionaryStatement;

    @Column(name = "pictogram", length = 500)
    private String pictogram;
}
