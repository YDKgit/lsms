package com.example.lsms.education.controller;

import com.example.lsms.education.dto.EduContentRequestDTO;
import com.example.lsms.education.dto.EduContentResponseDTO;
import com.example.lsms.education.service.EducationService;
import com.example.lsms.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCATION_MANAGER')")
    public CommonResponse<String> registerContent(@Valid @RequestBody EduContentRequestDTO dto) {
        String result = educationService.registerContent(dto);
        return CommonResponse.ok(result);
    }

    @GetMapping("/{contentId}")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<EduContentResponseDTO> getLearningProgressDetail(
            @PathVariable("contentId") Long contentId,
            @RequestAttribute("loginUserId") String loginUserId) {

        EduContentResponseDTO response = educationService.getLearningProgressDetail(loginUserId, contentId);
        return CommonResponse.ok(response);
    }

    @PostMapping("/{contentId}/progress")
    @PreAuthorize("isAuthenticated()")
    public CommonResponse<String> updateVideoProgress(
            @PathVariable("contentId") Long contentId,
            @RequestParam("lastViewedPoint") int lastViewedPoint,
            @RequestAttribute("loginUserId") String loginUserId) {

        String result = educationService.updateVideoProgress(loginUserId, contentId, lastViewedPoint);
        return CommonResponse.ok(result);
    }
}