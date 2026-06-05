package com.example.lsms.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),

    // 서브시스템별 조회 실패 (404 통일)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 사용자가 없습니다."),
    LAB_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 연구실이 없습니다."),
    CHEMICAL_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 화학물질이 없습니다."),
    WASTE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 폐기물이 없습니다."),
    WASTE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 폐기물 종류가 없습니다."),
    INSPECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 점검이 없습니다."),
    EDUCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 안전교육이 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
