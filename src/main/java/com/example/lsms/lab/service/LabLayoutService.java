package com.example.lsms.lab.service;

import com.example.lsms.lab.domain.FloorPlan;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.LabLayout;
import com.example.lsms.lab.dto.LabRequestDTO;
import com.example.lsms.lab.repository.FloorPlanRepository;
import com.example.lsms.lab.repository.LabLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabLayoutService {

    private final FloorPlanRepository floorPlanRepository;
    private final LabLayoutRepository labLayoutRepository;
    private final DrawingConverter drawingConverter;

    @Transactional
    public String processLabFiles(LabInfo lab, MultipartFile floorPlanFile) {
        if (floorPlanFile == null || floorPlanFile.isEmpty()) {
            return null;
        }
        return drawingConverter.convertDxfToPng(floorPlanFile, lab.getLabId());
    }

    @Transactional
    public void insertFloorPlan(LabInfo lab, String buildingName, Integer floorLevel, String filePath) {
        floorPlanRepository.save(FloorPlan.builder()
                .lab(lab)
                .buildingName(buildingName)
                .floorLevel(floorLevel)
                .filePath(filePath)
                .build());
    }

    @Transactional
    public void insertLayout(LabInfo lab, String filePath, String layoutData) {
        labLayoutRepository.save(LabLayout.builder()
                .lab(lab)
                .filePath(filePath)
                .layoutData(layoutData)
                .build());
    }

    public LabRequestDTO.FloorPlan getFloorPlanByLabId(Long labId) {
        return floorPlanRepository.findFirstByLab_LabIdOrderByPlanIdDesc(labId)
                .map(fp -> new LabRequestDTO.FloorPlan(
                        fp.getBuildingName(),
                        fp.getFloorLevel(),
                        fp.getFilePath()
                ))
                .orElse(null);
    }

    public LabRequestDTO.Layout getLayoutByLabId(Long labId) {
        return labLayoutRepository.findFirstByLab_LabIdOrderByLayoutIdDesc(labId)
                .map(layout -> new LabRequestDTO.Layout(
                        layout.getFilePath(),
                        layout.getLayoutData()
                ))
                .orElse(null);
    }

    public LabRequestDTO.FloorPlan getCombinedLabDetailsFloor(Long labId) {
        return getFloorPlanByLabId(labId);
    }

    public LabRequestDTO.Layout getCombinedLabDetailsLayout(Long labId) {
        return getLayoutByLabId(labId);
    }
}
