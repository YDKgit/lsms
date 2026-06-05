package com.example.lsms.lab.controller;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.lab.dto.LabResponse;
import com.example.lsms.lab.service.LabQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class LabQueryController {

    private final LabQueryService labQueryService;

    @GetMapping
    public CommonResponse<List<LabResponse>> getLabs() {
        return CommonResponse.ok(labQueryService.getLabs());
    }

    @GetMapping("/{labId}")
    public CommonResponse<LabResponse> getLab(@PathVariable Long labId) {
        return CommonResponse.ok(labQueryService.getLab(labId));
    }
}
