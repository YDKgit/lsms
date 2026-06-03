package com.example.lsms.global.security;

import com.example.lsms.global.exception.CustomException;
import com.example.lsms.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isValid(token)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 이후 컨트롤러에서 꺼내 쓸 수 있도록 request에 저장
        request.setAttribute("loginUserId", jwtUtil.getUserId(token));
        request.setAttribute("loginUserRole", jwtUtil.getRole(token));

        return true;
    }
}
