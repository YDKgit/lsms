package com.example.lsms.lab.service;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.lab.dto.LabResponse;
import com.example.lsms.lab.repository.LabInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LabQueryService {

    private final LabInfoRepository labInfoRepository;

    public List<LabResponse> getLabs() {
        return labInfoRepository.findAll().stream()
                .map(LabResponse::from)
                .toList();
    }

    public LabResponse getLab(Long labId) {
        return labInfoRepository.findById(labId)
                .map(LabResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.LAB_NOT_FOUND));
    }
}
