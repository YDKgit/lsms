package com.example.lsms.auth.controller;

import com.example.lsms.auth.dto.AuthDto;
import com.example.lsms.auth.service.AuthService;
import com.example.lsms.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CommonResponse<AuthDto.LoginResponse> login(@Valid @RequestBody AuthDto.LoginRequest request) {
        return CommonResponse.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public CommonResponse<AuthDto.LogoutResponse> logout() {
        return CommonResponse.ok(authService.logout());
    }
}
