package com.example.lsms.chemical.service;

import com.example.lsms.chemical.domain.Chemical;
import com.example.lsms.chemical.dto.ChemicalDto;
import com.example.lsms.chemical.repository.ChemicalRepository;
import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.repository.LabInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChemicalService {

    private final ChemicalRepository chemicalRepository;
    private final LabInfoRepository labInfoRepository;

    @Transactional
    public Chemical registerChemical(ChemicalDto.RegisterRequest request) {
        if (chemicalRepository.existsByCasNumber(request.casNumber())
                || chemicalRepository.existsByCatNumber(request.catNumber())) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        LabInfo lab = labInfoRepository.findById(request.labId())
                .orElseThrow(() -> new CustomException(ErrorCode.LAB_NOT_FOUND));

        return chemicalRepository.save(request.toEntity(lab));
    }

    public List<Chemical> getChemicals() {
        return chemicalRepository.findAll();
    }

    public Chemical getChemical(Long chemicalId) {
        return chemicalRepository.findById(chemicalId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHEMICAL_NOT_FOUND));
    }

    public Chemical getChemicalByCasNumber(String casNumber) {
        return chemicalRepository.findByCasNumber(casNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.CHEMICAL_NOT_FOUND));
    }

    public Chemical getChemicalByCatNumber(String catNumber) {
        return chemicalRepository.findByCatNumber(catNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.CHEMICAL_NOT_FOUND));
    }
}
