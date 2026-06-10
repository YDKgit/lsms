package com.example.lsms.chemical.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignalWord {

    DANGER("Danger"),
    WARNING("Warning");

    private final String label;

    SignalWord(String label) {
        this.label = label;
    }

    @JsonCreator
    public static SignalWord from(String value) {
        if (value == null) {
            return null;
        }

        for (SignalWord signalWord : values()) {
            if (signalWord.name().equalsIgnoreCase(value) || signalWord.label.equalsIgnoreCase(value)) {
                return signalWord;
            }
        }

        return null;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
