package com.example.lsms.inspection.controller;

import com.example.lsms.inspection.dto.ChecklistRequestDTO;
import com.example.lsms.inspection.dto.ChecklistResponseDTO;
import com.example.lsms.inspection.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Checklist API", description = "점검 항목(체크리스트) 관리 API")
@RestController
@RequestMapping("/api/checklist")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @Operation(summary = "체크리스트 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SAFETY_MANAGEMENT_TEAM')")
    public ResponseEntity<ChecklistResponseDTO> createItem(@Valid @RequestBody ChecklistRequestDTO request) {
        ChecklistResponseDTO response = checklistService.registerItem(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용 중인 체크리스트 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_SAFETY_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public ResponseEntity<List<ChecklistResponseDTO>> getAllActiveItems() {
        List<ChecklistResponseDTO> checklist = checklistService.getActiveChecklist();
        return ResponseEntity.ok(checklist);
    }

    @Operation(summary = "모든 체크리스트 조회 (비활성화 포함)")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'LAB_SAFETY_MANAGER', 'LAB_MANAGER', 'SAFETY_MANAGEMENT_TEAM')")
    public ResponseEntity<List<ChecklistResponseDTO>> getAllItems() {
        List<ChecklistResponseDTO> checklist = checklistService.getAllChecklistItems();
        return ResponseEntity.ok(checklist);
    }
}