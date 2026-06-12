package com.example.lsms.auth.dto;

import com.example.lsms.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDto {

    public record LoginRequest(
            @NotNull Long userId,
            @NotBlank String password
    ) {
    }

    public record LoginResponse(
            String accessToken,
            Long id,
            String userId,
            String name,
            UserRole role
    ) {
    }

    public record LogoutResponse(String message) {
    }
}