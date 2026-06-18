package com.example.lsms.user.dto;

import com.example.lsms.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    @Size(max = 50)
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 30)
    private String department;

    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String phoneNumber;

    @NotNull
    private UserRole role;
}
