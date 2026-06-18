package com.example.lsms.inspection.service;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.inspection.domain.Inspection;
import com.example.lsms.inspection.domain.InspectionDetail;
import com.example.lsms.inspection.dto.*;
import com.example.lsms.inspection.enums.InspectionType;
import com.example.lsms.inspection.repository.InspectionRepository;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.LabUserMapping;
import com.example.lsms.lab.repository.LabRepository;
import com.example.lsms.lab.repository.LabUserMappingRepository;
import com.example.lsms.user.domain.User;
import com.example.lsms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final LabRepository labRepository;
    private final UserRepository userRepository;
    private final LabUserMappingRepository labUserMappingRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public Long saveInspection(InspectionRequestDTO dto, String userRole) {
        if (dto.getLabID() == null || dto.getInspectorID() == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        LabInfo lab = labRepository.findById(dto.getLabID())
                .orElseThrow(() -> new CustomException(ErrorCode.LAB_NOT_FOUND));
        User inspector = userRepository.findById(dto.getInspectorID())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if ("LAB_SAFETY_MANAGER".equals(userRole) &&
                dto.getInspectionType() != InspectionType.DAILY) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 오프라인 스캔 파일 처리
        String scanFilePath = null;
        if (dto.getScanFile() != null && !dto.getScanFile().isEmpty()) {
            scanFilePath = saveFile(dto.getScanFile());
        }

        Inspection inspection = Inspection.builder()
                .lab(lab)
                .inspector(inspector)
                .inspectionDate(dto.getInspectionDate())
                .inspectionType(dto.getInspectionType())
                .inspectionMethod(dto.getInspectionMethod())
                .inspectionGrade(dto.getInspectionGrade())
                .attachedFilePath(scanFilePath)
                .build();

        // 지적사항 리스트 및 사진 파일 처리
        if (dto.getDetailList() != null) {
            for (InspectionDetailRequestDTO detailDto : dto.getDetailList()) {
                String detailFilePath = null;
                if (detailDto.getAttachedFile() != null && !detailDto.getAttachedFile().isEmpty()) {
                    detailFilePath = saveFile(detailDto.getAttachedFile());
                }

                InspectionDetail detail = InspectionDetail.builder()
                        .issueCategory(detailDto.getIssueCategory())
                        .problemDescribe(detailDto.getProblemDescribe())
                        .attachedFile(detailFilePath)
                        .build();

                inspection.addDetail(detail);
            }
        }

        Inspection saved = inspectionRepository.save(inspection);
        return saved.getInspectionId();
    }

    private String saveFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (originalFilename.contains("..")) {
                throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path targetLocation = uploadPath.resolve(newFileName);

            if (!targetLocation.startsWith(uploadPath)) {
                throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            }

            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }


    @Transactional(readOnly = true)
    public List<InspectionResponseDTO> getInspectionList(Long userId, String role) {
        List<Inspection> inspections;

        if ("SYSTEM_ADMIN".equals(role) || "SAFETY_MANAGEMENT_TEAM".equals(role)) {
            inspections = inspectionRepository.findAll();
        } else {
            inspections = inspectionRepository.findInspectionsByUserId(userId);
        }

        return inspections.stream()
                .map(inspection -> toSummaryDto(
                        inspection,
                        inspection.getLab().getLabName(),
                        inspection.getInspector().getName()))
                .collect(Collectors.toList());
    }


    @Transactional
    public InspectionResponseDTO getInspectionDetail(Long inspectionId, String role) {

        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSPECTION_NOT_FOUND));

        if ("LAB_MANAGER".equals(role) && inspection.getReadDateTime() == null) {
            inspection.updateReadDateTime();
        }

        return toDetailDto(inspection);
    }

    private InspectionResponseDTO toSummaryDto(Inspection inspection, String labName, String inspectorName) {
        List<InspectionDetailResponseDTO> details = inspection.getDetailList().stream()
                .map(detail -> InspectionDetailResponseDTO.builder()
                        .detailId(detail.getDetailId())
                        .issueCategory(detail.getIssueCategory())
                        .problemDescribe(detail.getProblemDescribe())
                        .attachedFile(detail.getAttachedFile())
                        .actionResult(detail.getActionResult())
                        .actionDate(detail.getActionDate())
                        .build())
                .collect(Collectors.toList());

        return InspectionResponseDTO.builder()
                .inspectionID(inspection.getInspectionId())
                .labName(labName)
                .inspectorName(inspectorName)
                .inspectionDate(inspection.getInspectionDate())
                .inspectionType(inspection.getInspectionType())
                .inspectionGrade(inspection.getInspectionGrade())
                .detailList(details)
                .build();
    }

    private InspectionResponseDTO toDetailDto(Inspection inspection) {
        List<InspectionDetailResponseDTO> details = inspection.getDetailList().stream()
                .map(detail -> InspectionDetailResponseDTO.builder()
                        .detailId(detail.getDetailId())
                        .issueCategory(detail.getIssueCategory())
                        .problemDescribe(detail.getProblemDescribe())
                        .attachedFile(detail.getAttachedFile())
                        .actionResult(detail.getActionResult())
                        .actionDate(detail.getActionDate())
                        .build())
                .collect(Collectors.toList());

        return InspectionResponseDTO.builder()
                .inspectionID(inspection.getInspectionId())
                .labName(inspection.getLab().getLabName())
                .inspectorName(inspection.getInspector().getName())
                .inspectionDate(inspection.getInspectionDate())
                .inspectionType(inspection.getInspectionType())
                .inspectionMethod(inspection.getInspectionMethod())
                .inspectionGrade(inspection.getInspectionGrade())
                .readDateTime(inspection.getReadDateTime())
                .detailList(details)
                .build();
    }

    @Transactional
    public void modifyActionStatus(Long detailId, String status) {
        int updatedRows = inspectionRepository.updateStatus(detailId, status);
        if (updatedRows == 0) {
            // 상세 ID 없을시
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }


    public void processEmailSending(Long inspectionId) {
        // 구현 X
    }

    public File createDownloadFile(Long inspectionId) {
        Inspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSPECTION_NOT_FOUND));

        // 일상점검이 아닌 경우 엑셀 다운로드 차단
        if (inspection.getInspectionType() != InspectionType.DAILY) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("점검 내역");

            String[] headers = {"연구실명", "연구실 책임자","점검자", "점검일", "점검 등급", "지적 사항", "조치 결과", "조치 일자", "책임자 확인 일시"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 데이터 생성
            int rowNum = 1;
            DateTimeFormatter dateFormatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (InspectionDetail detail : inspection.getDetailList()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(inspection.getLab().getLabName());
                row.createCell(1).setCellValue(inspection.getLab().getManager().getName());
                row.createCell(2).setCellValue(inspection.getInspector().getName());
                row.createCell(3).setCellValue(inspection.getInspectionDate().format(dateFormatter));
                row.createCell(4).setCellValue(inspection.getInspectionGrade() != null ? inspection.getInspectionGrade() : 0.0);
                row.createCell(5).setCellValue(detail.getProblemDescribe());
                row.createCell(6).setCellValue(detail.getActionResult() != null ? detail.getActionResult() : "미조치");
                row.createCell(7).setCellValue(detail.getActionDate() != null ? detail.getActionDate().format(dateTimeFormatter) : "");
                row.createCell(8).setCellValue(
                        inspection.getReadDateTime() != null ? inspection.getReadDateTime().format(dateTimeFormatter) : "미확인"
                );
            }

            // 임시 파일 생성
            File tempFile = File.createTempFile("inspection_report_", ".xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
                workbook.write(fileOut);
            }
            return tempFile;

        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CREATION_FAILED);
        }
    }

    public List<CalendarResponseDTO> getMonthlyCalendar(Long userId, String role, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Inspection> inspections = new ArrayList<>();

        if ("SYSTEM_ADMIN".equals(role) || "SAFETY_MANAGEMENT_TEAM".equals(role)) {
            // 관리자는 모든 연구실의 점검 내역을 조회
            List<LabInfo> allLabs = labRepository.findAll();
            for (LabInfo lab : allLabs) {
                inspections.addAll(inspectionRepository.findInspectionsByMonth(lab.getLabId(), startDate, endDate));
            }
        } else {
            // 일반 사용자는 자신이 속한 연구실의 점검 내역만 조회
            List<LabUserMapping> mappings = labUserMappingRepository.findByUser_Id(userId);
            if (mappings.isEmpty()) {
                return List.of(); // 속한 연구실이 없으면 빈 리스트 반환
            }
            for (LabUserMapping mapping : mappings) {
                inspections.addAll(inspectionRepository.findInspectionsByMonth(mapping.getLab().getLabId(), startDate, endDate));
            }
        }

        return inspections.stream()
                .map(CalendarResponseDTO::new)
                .collect(Collectors.toList());
    }
}