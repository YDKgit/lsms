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
            Long id,
            String userId,
            String name,
            UserRole role
    ) {
    }

    public record LogoutResponse(String message) {
    }
}