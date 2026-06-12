package com.example.lsms.inspection.controller;


import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.inspection.dto.CalendarRequestDTO;
import com.example.lsms.inspection.dto.CalendarResponseDTO;
import com.example.lsms.inspection.dto.InspectionRequestDTO;
import com.example.lsms.inspection.dto.InspectionResponseDTO;
import com.example.lsms.inspection.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Tag(name = "Inspection API", description = "점검 관리 서브시스템 API")
@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;

    @Operation(summary = "점검 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_MANAGER', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public CommonResponse<List<InspectionResponseDTO>> getInspectionList(
            @RequestAttribute("loginUserId") Long userId,
            @RequestAttribute("loginUserRole") String role
    ) {
        return CommonResponse.ok(inspectionService.getInspectionList(userId, role));
    }

    @Operation(summary = "점검 결과 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public CommonResponse<Long> registerInspection(@ModelAttribute InspectionRequestDTO dto, @RequestAttribute("loginUserRole") String role) {
        Long savedId = inspectionService.saveInspection(dto, role);
        return CommonResponse.ok(savedId);
    }

    @Operation(summary = "점검 상세 조회")
    @GetMapping("/{inspectionId}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_MANAGER', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public CommonResponse<InspectionResponseDTO> getInspectionDetail(@PathVariable Long inspectionId, @RequestAttribute("loginUserRole") String role) {
        return CommonResponse.ok(inspectionService.getInspectionDetail(inspectionId, role));
    }

    @Operation(summary = "조치 상태 갱신")
    @PatchMapping("/action/{detailId}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public CommonResponse<String> updateActionStatus(
            @PathVariable Long detailId,
            @RequestParam String status) {
        inspectionService.modifyActionStatus(detailId, status);
        return CommonResponse.ok("조치 상태 갱신 완료");
    }

    @Operation(summary = "점검 내역 다운로드")
    @GetMapping("/{inspectionId}/download")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_MANAGER', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public CommonResponse<File> requestDownload(@PathVariable Long inspectionId) {
        return CommonResponse.ok(inspectionService.createDownloadFile(inspectionId));
    }

    @Operation(summary = "월별 점검 달력 조회")
    @GetMapping("/calendar")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_MANAGER', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public ResponseEntity<List<CalendarResponseDTO>> getMonthlyCalendar(
            @Valid @ModelAttribute CalendarRequestDTO request,
            @RequestAttribute("loginUserId") Long userId,
            @RequestAttribute("loginUserRole") String role) {

        List<CalendarResponseDTO> response = inspectionService.getMonthlyCalendar(
                userId,
                role,
                request.getYear(),
                request.getMonth()
        );

        return ResponseEntity.ok(response);
    }
}