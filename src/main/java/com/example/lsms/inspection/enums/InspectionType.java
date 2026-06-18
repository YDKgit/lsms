package com.example.lsms.inspection.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InspectionType {

    DAILY("일상점검"),
    REGULAR("정기점검"),
    PRECISION("정밀안전진단"),
    OCCASIONAL("수시점검");

    private final String description;
}