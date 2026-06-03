package com.example.lsms.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER_MANAGER("User Manager"),                   //사용자관리자
    LAB_MANAGER("Lab Manager"),                     //연구실 책임자
    SYSTEM_ADMIN("System Administrator"),           //시스템 관리자
    SAFETY_MANAGEMENT_TEAM("Safety Management Team"), //안전관리팀
    LAB_SAFETY_MANAGER("Lab Safety Manager"),       //연구실 안전관리 담당자
    EDUCATION_MANAGER("Education Manager"),         //교육 담당자
    AUTHORITY_MANAGER("Authority Manager"),         //권한관리자
    RESEARCHER("Researcher");                       //연구원

    private final String label;
}










