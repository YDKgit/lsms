package com.example.lsms.lab.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class CadConversionAdapter implements DrawingConverter {

    private final Path uploadRoot;

    public CadConversionAdapter(@Value("${lsms.upload.path:uploads/lab}") String uploadPath) {
        this.uploadRoot = Paths.get(uploadPath);
    }

    @Override
    public String convertDxfToPng(MultipartFile dxfFile, Long labId) {
        try {
            Files.createDirectories(uploadRoot.resolve(String.valueOf(labId)));
            String original = dxfFile.getOriginalFilename();
            String baseName = original != null && original.contains(".")
                    ? original.substring(0, original.lastIndexOf('.'))
                    : "floorplan";
            String pngName = baseName + "_" + UUID.randomUUID().toString().substring(0, 8) + ".png";
            Path pngPath = uploadRoot.resolve(String.valueOf(labId)).resolve(pngName);

            // DXF→PNG 변환 대신 개발 단계에서는 원본 저장 후 경로 반환 (변환 어댑터 교체 가능)
            Path saved = uploadRoot.resolve(String.valueOf(labId)).resolve(
                    original != null ? original : "upload.dxf"
            );
            dxfFile.transferTo(saved);
            Files.copy(saved, pngPath);

            return toPublicUrl(pngPath);
        } catch (IOException e) {
            log.error("Failed to convert/store drawing file for labId={}", labId, e);
            throw new IllegalStateException("도면 파일 처리에 실패했습니다.");
        }
    }

    @Override
    public String storeLayoutPlan(MultipartFile file, Long labId) {
        try {
            Path layoutDir = uploadRoot.resolve(String.valueOf(labId)).resolve("layout");
            Files.createDirectories(layoutDir);
            String original = file.getOriginalFilename();
            String savedName = original != null ? original : "layout-plan.png";
            Path saved = layoutDir.resolve(savedName);
            file.transferTo(saved);
            return toPublicUrl(saved);
        } catch (IOException e) {
            log.error("Failed to store layout plan for labId={}", labId, e);
            throw new IllegalStateException("배치도 파일 처리에 실패했습니다.");
        }
    }

    private String toPublicUrl(Path savedFile) {
        Path uploadsRoot = uploadRoot.toAbsolutePath().normalize().getParent();
        Path absoluteSaved = savedFile.toAbsolutePath().normalize();
        String relative = uploadsRoot.relativize(absoluteSaved).toString().replace("\\", "/");
        return "/uploads/" + relative;
    }
}
