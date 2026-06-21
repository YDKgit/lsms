package com.example.lsms.waste.controller;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.waste.dto.WasteCreateRequest;
import com.example.lsms.waste.dto.WasteResponse;
import com.example.lsms.waste.dto.WasteSearchRequest;
import com.example.lsms.waste.dto.WasteTypeResponse;
import com.example.lsms.waste.service.WasteInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wastes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RESEARCHER', 'LAB_MANAGER', 'SYSTEM_ADMIN')")
public class WasteInfoController {

    private final WasteInfoService wasteInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<WasteResponse> createWaste(
            @Valid @RequestBody WasteCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long currentUserId = (Long) httpRequest.getAttribute("loginUserId");
        return CommonResponse.ok(wasteInfoService.createWaste(request, currentUserId));
    }

    @GetMapping("/{wasteId}")
    public CommonResponse<WasteResponse> getWaste(@PathVariable Long wasteId) {
        return CommonResponse.ok(wasteInfoService.getWaste(wasteId));
    }

    @GetMapping
    public CommonResponse<List<WasteResponse>> searchWastes(
            @RequestParam(required = false) String wasteName,
            @RequestParam(required = false) String wasteTypeCode,
            @RequestParam(required = false) Long generatedLabId,
            @RequestParam(required = false) String storageLocation,
            @RequestParam(required = false) com.example.lsms.waste.domain.WasteStatus status
    ) {
        WasteSearchRequest request = new WasteSearchRequest(
                wasteName, wasteTypeCode, generatedLabId, storageLocation, status
        );
        return CommonResponse.ok(wasteInfoService.searchWastes(request));
    }

    @GetMapping("/types")
    public CommonResponse<List<WasteTypeResponse>> getWasteTypes() {
        return CommonResponse.ok(wasteInfoService.getWasteTypes());
    }
}
