package com.example.lsms.lab.controller;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.dto.LabRequestDTO;
import com.example.lsms.lab.dto.LabResponseDTO;
import com.example.lsms.lab.service.LabEquipmentService;
import com.example.lsms.lab.service.LabLayoutService;
import com.example.lsms.lab.service.LabMasterService;
import com.example.lsms.lab.service.LabRegistrationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/labs")
@RequiredArgsConstructor
public class LabController {

    private final LabMasterService labMasterService;
    private final LabEquipmentService labEquipmentService;
    private final LabLayoutService labLayoutService;
    private final LabRegistrationFacade labRegistrationFacade;

    @GetMapping("/form")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER','LAB_SAFETY_MANAGER','RESEARCHER')")
    public CommonResponse<LabResponseDTO.FormOptions> getRegistrationForm() {
        return CommonResponse.ok(new LabResponseDTO.FormOptions(
                List.of("GENERAL", "CHEMICAL", "BIO"),
                List.of("A", "B", "C"),
                List.of("Y", "N")
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER','LAB_SAFETY_MANAGER','SAFETY_MANAGEMENT_TEAM','RESEARCHER')")
    public CommonResponse<List<LabResponseDTO.Summary>> getLabList() {
        return CommonResponse.ok(labMasterService.getLabList());
    }

    @GetMapping("/{labId}")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER','LAB_SAFETY_MANAGER','SAFETY_MANAGEMENT_TEAM','RESEARCHER')")
    public CommonResponse<LabResponseDTO> getLabDetail(@PathVariable Long labId) {
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        List<LabResponseDTO.Equip> equipments = labEquipmentService.getEquipmentsByLabId(labId);
        LabRequestDTO.FloorPlan floorPlan = labLayoutService.getCombinedLabDetailsFloor(labId);
        LabRequestDTO.Layout layout = labLayoutService.getCombinedLabDetailsLayout(labId);
        return CommonResponse.ok(buildLabResponse(lab, equipments, floorPlan, layout));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<LabResponseDTO.Created> createLabMaster(
            @Valid @RequestBody LabRequestDTO.MasterCreate request
    ) {
        return CommonResponse.ok(labMasterService.createLabMaster(request));
    }

    @PostMapping("/{labId}/equipments")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<String> registerEquipments(
            @PathVariable Long labId,
            @Valid @RequestBody List<LabRequestDTO.Equip> equipList
    ) {
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        labEquipmentService.batchInsertEquipments(lab, equipList);
        return CommonResponse.ok("Safety equipments saved");
    }

    @PostMapping("/{labId}/floor-plan")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<LabResponseDTO.FloorPlanUploaded> uploadFloorPlan(
            @PathVariable Long labId,
            @RequestParam(required = false) String buildingName,
            @RequestParam(required = false) Integer floorLevel,
            @RequestPart(required = false) MultipartFile file
    ) {
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = labLayoutService.processLabFiles(lab, file);
            labLayoutService.insertFloorPlan(lab, buildingName, floorLevel, filePath);
        }
        return CommonResponse.ok(new LabResponseDTO.FloorPlanUploaded(filePath));
    }

    @PostMapping("/{labId}/layout/plan")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<LabResponseDTO.LayoutPlanUploaded> uploadLayoutPlan(
            @PathVariable Long labId,
            @RequestPart MultipartFile file
    ) {
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        String filePath = labLayoutService.processLayoutPlanFile(lab, file);
        return CommonResponse.ok(new LabResponseDTO.LayoutPlanUploaded(filePath));
    }

    @PutMapping("/{labId}/layout")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<String> saveCanvasLayout(
            @PathVariable Long labId,
            @Valid @RequestBody LabRequestDTO.LayoutSave request
    ) {
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        labLayoutService.insertLayout(lab, request.filePath(), request.layoutData());
        return CommonResponse.ok("Layout saved");
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','LAB_MANAGER')")
    public CommonResponse<LabResponseDTO> registerLabInfo(
            @Valid @RequestBody LabRequestDTO request
    ) {
        LabResponseDTO.Created created = labRegistrationFacade.registerLabInfo(request);

        Long labId = created.labId();
        LabInfo lab = labMasterService.getLabMasterInfo(labId);
        List<LabResponseDTO.Equip> equipments = labEquipmentService.getEquipmentsByLabId(labId);
        LabRequestDTO.FloorPlan floorPlan = labLayoutService.getCombinedLabDetailsFloor(labId);
        LabRequestDTO.Layout layout = labLayoutService.getCombinedLabDetailsLayout(labId);
        return CommonResponse.ok(buildLabResponse(lab, equipments, floorPlan, layout));
    }

    private LabResponseDTO buildLabResponse(
            LabInfo lab,
            List<LabResponseDTO.Equip> equipments,
            LabRequestDTO.FloorPlan floorPlan,
            LabRequestDTO.Layout layout
    ) {
        String managerName = lab.getManager() != null ? lab.getManager().getName() : null;
        String managerDepartment = lab.getManager() != null ? lab.getManager().getDepartment() : null;
        String floorPlanUrl = floorPlan != null ? floorPlan.filePath() : null;
        String layoutUrl = layout != null ? layout.filePath() : null;
        String layoutData = layout != null ? layout.layoutData() : null;

        return new LabResponseDTO(
                lab.getLabId(),
                lab.getLabName(),
                null,
                managerName,
                managerDepartment,
                lab.getLocation(),
                lab.getLabType(),
                lab.getIsInspectionTarget(),
                lab.getContact(),
                lab.getGrade(),
                floorPlanUrl,
                layoutUrl,
                layoutData,
                equipments
        );
    }
}
