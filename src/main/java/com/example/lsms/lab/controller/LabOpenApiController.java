package com.example.lsms.lab.controller;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.lab.dto.LabResponseDTO;
import com.example.lsms.lab.service.LabMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labs/open")
@RequiredArgsConstructor
public class LabOpenApiController {

    private final LabMasterService labMasterService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER','SAFETY_MANAGEMENT_TEAM','RESEARCHER')")
    public CommonResponse<List<LabResponseDTO.Dashboard>> getLabStatusForDashboard() {
        return CommonResponse.ok(labMasterService.getDashboardStatus());
    }
}
