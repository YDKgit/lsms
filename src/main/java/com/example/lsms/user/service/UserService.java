package com.example.lsms.user.service;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import com.example.lsms.user.domain.User;
import com.example.lsms.user.domain.UserRole;
import com.example.lsms.user.dto.UserRegisterRequest;
import com.example.lsms.user.dto.UserResponse;
import com.example.lsms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new CustomException(ErrorCode.USER_ID_DUPLICATE);
        }

        User user = User.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .department(request.getDepartment())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    public List<UserResponse> getUsers(String department) {
        List<User> users = StringUtils.hasText(department)
                ? userRepository.findByDepartment(department)
                : userRepository.findAll();
        return users.stream().map(UserResponse::from).collect(Collectors.toList());
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    public void validateSelfOrAdmin(Long loginUserId, String loginUserRole, Long targetUserId) {
        if (!loginUserId.equals(targetUserId) && !UserRole.SYSTEM_ADMIN.name().equals(loginUserRole)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}
