package com.example.lsms.auth.dto;

import com.example.lsms.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {

    public record LoginRequest(
            @NotBlank String userId,
            @NotBlank String password
    ) {
    }

    public record LoginResponse(
            String accessToken,
            Long userId,
            UserRole role
    ) {
    }

    public record LogoutResponse(String message) {
    }
}
