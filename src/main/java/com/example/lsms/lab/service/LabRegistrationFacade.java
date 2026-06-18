package com.example.lsms.lab.service;

import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.dto.LabRequestDTO;
import com.example.lsms.lab.dto.LabResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 연구실 통합 등록(마스터 + 안전장비 + 평면도 + 배치도)을 단일 트랜잭션으로 처리.
 * 중간 단계 실패 시 전체 롤백.
 */
@Service
@RequiredArgsConstructor
public class LabRegistrationFacade {

    private final LabMasterService labMasterService;
    private final LabEquipmentService labEquipmentService;
    private final LabLayoutService labLayoutService;

    @Transactional
    public LabResponseDTO.Created registerLabInfo(LabRequestDTO request) {
        LabResponseDTO.Created created = labMasterService.createLabMaster(request.toMasterCreate());
        LabInfo lab = labMasterService.getLabMasterInfo(created.labId());

        labEquipmentService.batchInsertEquipments(lab, request.equipList());

        if (request.floorPlan() != null && request.floorPlan().filePath() != null) {
            labLayoutService.insertFloorPlan(
                    lab,
                    request.floorPlan().buildingName(),
                    request.floorPlan().floorLevel(),
                    request.floorPlan().filePath()
            );
        }
        if (request.layout() != null) {
            labLayoutService.insertLayout(
                    lab,
                    request.layout().filePath(),
                    request.layout().layoutData()
            );
        }

        return created;
    }
}
