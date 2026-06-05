package com.example.lsms.waste.dto;

import com.example.lsms.waste.domain.WasteStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WasteUpdateRequest(
        @NotBlank(message = "폐기물명을 입력해야 합니다.")
        @Size(max = 100, message = "폐기물명은 100자 이하여야 합니다.")
        String wasteName,

        @NotBlank(message = "폐기물 종류 코드를 선택해야 합니다.")
        String wasteTypeCode,

        @NotNull(message = "발생 연구실을 선택해야 합니다.")
        Long generatedLabId,

        @NotBlank(message = "보관 위치를 입력해야 합니다.")
        @Size(max = 100, message = "보관 위치는 100자 이하여야 합니다.")
        String storageLocation,

        @NotNull(message = "폐기물 상태를 선택해야 합니다.")
        WasteStatus status
) {
}
