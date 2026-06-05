package com.example.lsms.waste.service;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.repository.LabInfoRepository;
import com.example.lsms.user.domain.User;
import com.example.lsms.user.repository.UserRepository;
import com.example.lsms.waste.domain.WasteInfo;
import com.example.lsms.waste.domain.WasteStatus;
import com.example.lsms.waste.domain.WasteType;
import com.example.lsms.waste.dto.WasteCreateRequest;
import com.example.lsms.waste.dto.WasteResponse;
import com.example.lsms.waste.dto.WasteSearchRequest;
import com.example.lsms.waste.dto.WasteTypeResponse;
import com.example.lsms.waste.dto.WasteUpdateRequest;
import com.example.lsms.waste.repository.WasteInfoRepository;
import com.example.lsms.waste.repository.WasteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WasteInfoService {

    private final WasteInfoRepository wasteInfoRepository;
    private final WasteTypeRepository wasteTypeRepository;
    private final LabInfoRepository labInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public WasteResponse createWaste(WasteCreateRequest request, Long currentUserId) {
        User registeredBy = findUser(currentUserId);
        LabInfo generatedLab = findLab(request.generatedLabId());
        WasteType wasteType = findWasteType(request.wasteTypeCode());

        WasteInfo wasteInfo = WasteInfo.builder()
                .wasteName(request.wasteName().trim())
                .wasteType(wasteType)
                .generatedLab(generatedLab)
                .storageLocation(request.storageLocation().trim())
                .registeredBy(registeredBy)
                .registeredAt(LocalDateTime.now())
                .status(WasteStatus.REGISTERED)
                .build();

        return WasteResponse.from(wasteInfoRepository.save(wasteInfo));
    }

    public WasteResponse getWaste(Long wasteId) {
        return WasteResponse.from(findWaste(wasteId));
    }

    public List<WasteResponse> searchWastes(WasteSearchRequest request) {
        return wasteInfoRepository.searchActive(
                        blankToNull(request.wasteName()),
                        blankToNull(request.wasteTypeCode()),
                        request.generatedLabId(),
                        blankToNull(request.storageLocation()),
                        request.status(),
                        WasteStatus.DELETED
                ).stream()
                .map(WasteResponse::from)
                .toList();
    }

    public List<WasteTypeResponse> getWasteTypes() {
        return wasteTypeRepository.findAll().stream()
                .map(WasteTypeResponse::from)
                .toList();
    }

    @Transactional
    public WasteResponse updateWaste(Long wasteId, WasteUpdateRequest request) {
        WasteInfo wasteInfo = findWaste(wasteId);
        LabInfo generatedLab = findLab(request.generatedLabId());
        WasteType wasteType = findWasteType(request.wasteTypeCode());

        if (request.status() == WasteStatus.DELETED) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        wasteInfo.update(
                request.wasteName().trim(),
                wasteType,
                generatedLab,
                request.storageLocation().trim(),
                request.status()
        );
        return WasteResponse.from(wasteInfo);
    }

    @Transactional
    public void deleteWaste(Long wasteId) {
        findWaste(wasteId).markDeleted();
    }

    private WasteInfo findWaste(Long wasteId) {
        return wasteInfoRepository.findActiveById(wasteId, WasteStatus.DELETED)
                .orElseThrow(() -> new CustomException(ErrorCode.WASTE_NOT_FOUND));
    }

    private WasteType findWasteType(String code) {
        return wasteTypeRepository.findById(code)
                .orElseThrow(() -> new CustomException(ErrorCode.WASTE_TYPE_NOT_FOUND));
    }

    private LabInfo findLab(Long labId) {
        return labInfoRepository.findById(labId)
                .orElseThrow(() -> new CustomException(ErrorCode.LAB_NOT_FOUND));
    }

    private User findUser(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
