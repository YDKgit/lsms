# LSMS

## 1. 공통 규칙

### 1-1. CommonResponse 구조

- 공통 응답 래퍼: `CommonResponse<T>`
- 필드:
  - `success: boolean`
  - `data: T`
- 성공: `CommonResponse.ok(data)`
- 실패: `CommonResponse.fail(message)`



### 1-2. 인증 키 이름 규칙

- 인증 방식: JWT
- 요청 헤더:
  - `Authorization: Bearer {token}`
- 인터셉터에서 request attribute 저장:
  - `loginUserId`
  - `loginUserRole`

### 1-3. 예외 응답 규칙

- 전역 예외: `@RestControllerAdvice`
- 에러도 `CommonResponse.fail()`로 반환
- 상태코드 규칙:
  - `401`: 인증 실패 (`UNAUTHORIZED`)
  - `403`: 권한 없음 (`FORBIDDEN`)
  - `404`: 리소스 없음 (`*_NOT_FOUND`)
  - `500`: 서버 오류

---

## 2. global 세팅

완료 항목:
- CommonResponse 작성
- `@ControllerAdvice` 예외처리
- CORS 설정
- JWT 인증 설정
- Swagger 설정
- Spring Security 설정
- Method Security 설정 (`@EnableMethodSecurity`)


### Auth 

- 각 엔티티 구현후 로그인로직 구현예정
- `POST /api/auth/login`
- `POST /api/auth/logout`

---

## 서브시스템별 API 주소 표준 
추가적인 기능은 하위 경로 추가

### User (`/api/users`)

- `POST /api/users` : 사용자 등록
- `GET /api/users` : 사용자 목록
- `GET /api/users/{userId}` : 사용자 조회


### Lab (`/api/labs`)

- `POST /api/labs` : 연구실 등록
- `GET /api/labs` : 연구실 목록
- `GET /api/labs/{labId}` : 연구실 조회


### Chemical (`/api/chemicals`)

- `POST /api/chemicals` : 화학물질 등록
- `GET /api/chemicals` : 화학물질 목록
- `GET /api/chemicals/{chemicalId}` : 화학물질 조회

### Waste (`/api/wastes`)

- `POST /api/wastes` : 폐기물 등록
- `GET /api/wastes` : 폐기물 목록
- `GET /api/wastes/{wasteId}` : 폐기물 조회

### Inspection (`/api/inspections`)

- `POST /api/inspections` : 점검 등록
- `GET /api/inspections` : 점검 목록
- `GET /api/inspections/{inspectionId}` : 점검 조회

### Education (`/api/educations`)

- `POST /api/educations` : 교육 콘텐츠 등록
- `GET /api/educations` : 교육 콘텐츠 목록
- `GET /api/educations/{contentId}` : 교육 콘텐츠 조회

---

## 권한/Role 구현 방식

### 1) User 엔티티에 enum 저장

`User` 엔티티의 `role`은 enum 타입으로 두고, DB에는 문자열로 저장.

```java
@Entity
public class User {

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
```

서비스/등록 로직에서 enum 상수를 엔티티에 넣고 저장.

```java
User user = User.builder()
    .role(UserRole.SYSTEM_ADMIN)
    .build();

userRepository.save(user);
```

### 2) 컨트롤러 진입 전 권한 필터링

접근 권한은 `@PreAuthorize`로 controller에서 필터링


```java
@PreAuthorize("hasRole('LAB_MANAGER') or hasRole('SYSTEM_ADMIN')")
```


서비스에서 권한 검증을 하는것으로 설계했다면 서비스 계층에서 수행



## 요약


현재 구조는 6개 서브시스템이 병렬로 개발 가능

이미 준비된 것들:
- 공통 응답 형식 (`CommonResponse`)
- 공통 예외 처리 (`GlobalExceptionHandler`, `ErrorCode`)
- 공통 인증     (`JwtUtil`, `JwtInterceptor`)
- 공통 권한 enum (`UserRole`)
- 공통 URL 규칙 (`/api/{domain}`)

H2 데이터베이스 ddl-auto=create로 자동 생성해서 빠르게 개발
이후 MySQL로 전환 

---

## 구현 주의점
-세션에서 공통으로 사용하는 사용자 정보: userId, role.  이외 사용자 정보는 필요 시 DB 조회
- URL은 고정 (`/api/...` 고정)
- 컨트롤러 응답은 반드시 `CommonResponse`로
- 예외는 `CustomException` + `ErrorCode` 사용
- 권한값은 `UserRole` enum 문자열(`name()`) 기준으로 처리 예) LAB_MANAGER

---
