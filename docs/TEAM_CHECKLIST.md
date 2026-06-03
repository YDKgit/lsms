# LSMS — 해야 할 것 / 이미 된 것

---

## 1. DB/엔티티 확정 (회의)

칼럼명 일치 ❌ 팀 합의
외래키 추가 ❌ 팀 합의

→ 합의 후: `user|lab|chemical|waste|inspection|education` / `domain`

---

## 2. 공통 규칙 합의 (회의)

CommonResponse 구조 ❌ 팀 합의
API URL 규칙 ❌ 팀 합의
세션 키 이름 ❌ 팀 합의
예외 응답 형태 ❌ 팀 합의

→ 초안만 있음 (`global` — 회의 후 수정)

---

## 3. global 세팅 (혼자)

CommonResponse 작성 ✅ 혼자 가능
@ControllerAdvice 예외처리 ✅ 혼자 가능
CORS 설정 ✅ 혼자 가능
세션 인증 설정 ✅ 혼자 가능
Spring Boot 프로젝트 생성 ✅ 혼자 가능
Swagger 설정 ✅ 혼자 가능
Git 저장소 생성 ✅ 혼자 가능
완료 후 전체 공유 ❌ 팀 공유 필요

→ 코드 위치: `global/` (common, config, exception, security)

---

## 4. 서브시스템 간 인터페이스 합의 (회의)

필요한 Service 메서드 목록 정리 ❌ 팀 합의

---

## 5. 각자 개발

의존관계 있는 서브시스템 순서 조율 ❌ 팀 합의
각자 서브시스템 개발 ✅ 혼자 가능

→ 패키지 골격만 있음 (`user`, `lab`, `chemical`, `waste`, `inspection`, `education` — controller/service/repository/domain/dto)

---

## 6. 통합 + 프론트 연동

머지 + 충돌 해결 ❌ 팀 협업
프론트 연동 테스트 ❌ 팀 협업
