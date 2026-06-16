package com.example.lsms.user.controller;

import com.example.lsms.global.common.CommonResponse;
import com.example.lsms.user.dto.UserRegisterRequest;
import com.example.lsms.user.dto.UserResponse;
import com.example.lsms.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('USER_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<CommonResponse<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.ok(response));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<CommonResponse<List<UserResponse>>> getUsers(
            @RequestParam(required = false) String department) {
        List<UserResponse> users = userService.getUsers(department);
        return ResponseEntity.ok(CommonResponse.ok(users));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<UserResponse>> getUser(
            @PathVariable Long userId,
            HttpServletRequest request) {
        Long loginUserId = (Long) request.getAttribute("loginUserId");
        String loginUserRole = (String) request.getAttribute("loginUserRole");
        userService.validateSelfOrAdmin(loginUserId, loginUserRole, userId);
        UserResponse response = userService.getUser(userId);
        return ResponseEntity.ok(CommonResponse.ok(response));
    }
}
