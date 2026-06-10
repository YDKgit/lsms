package com.example.lsms.chemical.controller;

import com.example.lsms.chemical.domain.Chemical;
import com.example.lsms.chemical.dto.ChemicalDto;
import com.example.lsms.chemical.service.ChemicalService;
import com.example.lsms.global.common.CommonResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chemicals")
@RequiredArgsConstructor
@Validated
public class ChemicalController {

    private final ChemicalService chemicalService;

    @PostMapping
    public CommonResponse<ChemicalDto.Response> registerChemical(
            @Valid @RequestBody ChemicalDto.RegisterRequest request
    ) {
        return CommonResponse.ok(ChemicalDto.Response.from(chemicalService.registerChemical(request)));
    }

    @GetMapping
    public CommonResponse<List<ChemicalDto.Response>> getChemicals() {
        List<ChemicalDto.Response> responses = chemicalService.getChemicals().stream()
                .map(ChemicalDto.Response::from)
                .toList();
        return CommonResponse.ok(responses);
    }

    @GetMapping("/{chemicalId}")
    public CommonResponse<ChemicalDto.Response> getChemical(@Positive @PathVariable Long chemicalId) {
        return CommonResponse.ok(ChemicalDto.Response.from(chemicalService.getChemical(chemicalId)));
    }

    @GetMapping("/cas/{casNumber}")
    public CommonResponse<ChemicalDto.Response> getChemicalByCasNumber(
            @NotBlank @PathVariable String casNumber
    ) {
        Chemical chemical = chemicalService.getChemicalByCasNumber(casNumber);
        return CommonResponse.ok(ChemicalDto.Response.from(chemical));
    }

    @GetMapping("/cat/{catNumber}")
    public CommonResponse<ChemicalDto.Response> getChemicalByCatNumber(
            @NotBlank @PathVariable String catNumber
    ) {
        Chemical chemical = chemicalService.getChemicalByCatNumber(catNumber);
        return CommonResponse.ok(ChemicalDto.Response.from(chemical));
    }
}
