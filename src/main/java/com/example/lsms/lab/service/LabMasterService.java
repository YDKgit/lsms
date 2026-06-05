package com.example.lsms.lab.service;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.LabUserMapping;
import com.example.lsms.lab.dto.LabRequestDTO;
import com.example.lsms.lab.dto.LabResponseDTO;
import com.example.lsms.lab.repository.LabRepository;
import com.example.lsms.lab.repository.LabUserMappingRepository;
import com.example.lsms.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabMasterService {

    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final LabUserMappingRepository labUserMappingRepository;

    @Transactional
    public LabResponseDTO.Created createLabMaster(LabRequestDTO.MasterCreate request) {
        validateLabData(request.buildingLocation(), request.labName());

        User manager = userRepository.findById(request.managerId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LabInfo lab = LabInfo.builder()
                .deptId(request.deptId())
                .manager(manager)
                .labName(request.labName())
                .location(request.buildingLocation())
                .labType(request.labType())
                .isInspectionTarget(request.isInspectionTarget())
                .contact(request.contact())
                .grade(request.grade())
                .signImagePath(request.signImagePath())
                .photoImagePath(request.photoImagePath())
                .build();

        LabInfo saved = labRepository.save(lab);
        mapLabUsers(saved, request.memberUserIds());
        return new LabResponseDTO.Created(saved.getLabId());
    }

    public LabInfo getLabMasterInfo(Long labId) {
        return labRepository.findByLabId(labId)
                .orElseThrow(() -> new CustomException(ErrorCode.LAB_NOT_FOUND));
    }

    public List<LabResponseDTO.Summary> getLabList() {
        return labRepository.findAllLabs().stream()
                .map(LabResponseDTO.Summary::from)
                .toList();
    }

    public List<LabResponseDTO.Dashboard> getDashboardStatus() {
        return labRepository.findAllLabs().stream()
                .map(LabResponseDTO.Dashboard::from)
                .toList();
    }

    public void validateLabData(String location, String labName) {
        if (labName == null || labName.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (location == null || location.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (labRepository.existsByLocation(location)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    @Transactional
    public void mapLabUsers(LabInfo lab, List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            labUserMappingRepository.save(LabUserMapping.builder()
                    .lab(lab)
                    .user(user)
                    .build());
        }
    }
}
