package com.example.lsms.auth.service;

import com.example.lsms.auth.dto.AuthDto;
import com.example.lsms.global.security.JwtUtil;
import com.example.lsms.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        UserRole role = resolveRole(request.userId());
        String token = jwtUtil.generateToken(request.userId(), role.name());
        return new AuthDto.LoginResponse(token, request.userId(), role);
    }

    public AuthDto.LogoutResponse logout() {
        return new AuthDto.LogoutResponse("Logged out");
    }

    private UserRole resolveRole(Long userId) {
        // Temporary policy for shared dev environment.
        return userId != null && userId == 1L ? UserRole.SYSTEM_ADMIN : UserRole.RESEARCHER;
    }
}
