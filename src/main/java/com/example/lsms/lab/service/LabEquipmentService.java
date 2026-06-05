package com.example.lsms.lab.service;

import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.SafetyEquipment;
import com.example.lsms.lab.dto.LabRequestDTO;
import com.example.lsms.lab.dto.LabResponseDTO;
import com.example.lsms.lab.repository.SafetyEquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabEquipmentService {

    private final SafetyEquipmentRepository safetyEquipmentRepository;

    @Transactional
    public void batchInsertEquipments(LabInfo lab, List<LabRequestDTO.Equip> equipList) {
        if (equipList == null || equipList.isEmpty()) {
            return;
        }
        List<SafetyEquipment> entities = new ArrayList<>();
        for (LabRequestDTO.Equip dto : equipList) {
            String category = dto.category();
            if (dto.installedLocation() != null && !dto.installedLocation().isBlank()) {
                category = dto.installedLocation();
            }
            entities.add(SafetyEquipment.builder()
                    .lab(lab)
                    .equipName(dto.equipName())
                    .category(category)
                    .quantity(dto.quantity())
                    .status(dto.status())
                    .build());
        }
        safetyEquipmentRepository.saveAll(entities);
    }

    public List<LabResponseDTO.Equip> getEquipmentsByLabId(Long labId) {
        return safetyEquipmentRepository.findByLab_LabId(labId).stream()
                .map(LabResponseDTO.Equip::from)
                .toList();
    }
}
