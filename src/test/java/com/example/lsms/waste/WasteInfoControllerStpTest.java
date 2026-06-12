package com.example.lsms.waste;

import com.example.lsms.lab.repository.LabInfoRepository;
import com.example.lsms.user.domain.User;
import com.example.lsms.user.domain.UserRole;
import com.example.lsms.user.repository.UserRepository;
import com.example.lsms.waste.repository.WasteInfoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WasteInfoControllerStpTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LabInfoRepository labInfoRepository;

    @Autowired
    private WasteInfoRepository wasteInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        /*
         * UTC-WASTE-426 테스트용 사용자
         * Waste API 접근 권한이 없는 USER_MANAGER 역할
         */
        if (!userRepository.existsById(99L)) {
            userRepository.save(User.builder()
                    .id(99L)
                    .userId("noauth")
                    .password(passwordEncoder.encode("1"))
                    .name("권한없는사용자")
                    .department("테스트부서")
                    .phoneNumber("010-9999-9999")
                    .role(UserRole.USER_MANAGER)
                    .build());
        }
    }

    private String login(String userId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "userId", userId,
                                "password", "1"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data")
                .path("accessToken")
                .asText();
    }

    private Long existingLabId() {
        return labInfoRepository.findAll().get(0).getLabId();
    }

    private Long existingWasteId() {
        return wasteInfoRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("UTC-WASTE-411 폐기물 정상 등록 확인")
    void createWaste_success() throws Exception {
        String token = login("researcher");
        Long labId = existingLabId();

        mockMvc.perform(post("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wasteName", "폐산 용액",
                                "wasteTypeCode", "EXPERIMENTAL_WASTE_LIQUID",
                                "generatedLabId", labId,
                                "storageLocation", "A-101"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.wasteName").value("폐산 용액"))
                .andExpect(jsonPath("$.data.wasteTypeCode").value("EXPERIMENTAL_WASTE_LIQUID"))
                .andExpect(jsonPath("$.data.generatedLabId").value(labId))
                .andExpect(jsonPath("$.data.storageLocation").value("A-101"))
                .andExpect(jsonPath("$.data.status").value("REGISTERED"));
    }

    @Test
    @DisplayName("UTC-WASTE-412 필수 입력값 누락 시 등록 실패 확인")
    void createWaste_blankWasteName_fail() throws Exception {
        String token = login("researcher");
        Long labId = existingLabId();

        mockMvc.perform(post("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wasteName", "",
                                "wasteTypeCode", "EXPERIMENTAL_WASTE_LIQUID",
                                "generatedLabId", labId,
                                "storageLocation", "A-101"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("폐기물명을 입력해야 합니다."));
    }

    @Test
    @DisplayName("UTC-WASTE-413 존재하지 않는 폐기물 종류 입력 시 등록 실패 확인")
    void createWaste_wrongWasteType_fail() throws Exception {
        String token = login("researcher");
        Long labId = existingLabId();

        mockMvc.perform(post("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wasteName", "폐시약",
                                "wasteTypeCode", "WRONG_TYPE",
                                "generatedLabId", labId,
                                "storageLocation", "A-101"
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("등록된 폐기물 종류가 없습니다."));
    }

    @Test
    @DisplayName("UTC-WASTE-414 존재하지 않는 연구실 입력 시 등록 실패 확인")
    void createWaste_wrongLab_fail() throws Exception {
        String token = login("researcher");

        mockMvc.perform(post("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "wasteName", "폐산 용액",
                                "wasteTypeCode", "EXPERIMENTAL_WASTE_LIQUID",
                                "generatedLabId", 9999L,
                                "storageLocation", "A-101"
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("등록된 연구실이 없습니다."));
    }

    @Test
    @DisplayName("UTC-WASTE-421 폐기물 목록 정상 조회 확인")
    void getWasteList_success() throws Exception {
        String token = login("researcher");

        mockMvc.perform(get("/api/wastes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("UTC-WASTE-422 검색 조건으로 폐기물 목록 조회 확인")
    void searchWasteList_success() throws Exception {
        String token = login("researcher");
        Long labId = existingLabId();

        mockMvc.perform(get("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .param("wasteName", "폐산")
                        .param("wasteTypeCode", "EXPERIMENTAL_WASTE_LIQUID")
                        .param("generatedLabId", String.valueOf(labId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data[0].wasteName").value(containsString("폐산")))
                .andExpect(jsonPath("$.data[0].wasteTypeCode").value("EXPERIMENTAL_WASTE_LIQUID"))
                .andExpect(jsonPath("$.data[0].generatedLabId").value(labId));
    }

    @Test
    @DisplayName("UTC-WASTE-423 잘못된 검색 조건 입력 시 조회 실패 확인")
    void searchWasteList_wrongStatus_fail() throws Exception {
        String token = login("researcher");

        mockMvc.perform(get("/api/wastes")
                        .header("Authorization", "Bearer " + token)
                        .param("status", "WRONG_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("잘못된 입력값입니다."));
    }

    @Test
    @DisplayName("UTC-WASTE-424 폐기물 상세 정보 정상 조회 확인")
    void getWasteDetail_success() throws Exception {
        String token = login("researcher");
        Long wasteId = existingWasteId();

        mockMvc.perform(get("/api/wastes/{wasteId}", wasteId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(wasteId));
    }

    @Test
    @DisplayName("UTC-WASTE-425 존재하지 않거나 삭제된 폐기물 상세 조회 실패 확인")
    void getWasteDetail_notFound_fail() throws Exception {
        String token = login("researcher");

        mockMvc.perform(get("/api/wastes/{wasteId}", 99999999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("등록된 폐기물이 없습니다."));
    }

    @Test
    @DisplayName("UTC-WASTE-426 권한 없는 사용자 폐기물 조회 차단 확인")
    void getWasteList_forbidden_fail() throws Exception {
        String token = login("noauth");

        mockMvc.perform(get("/api/wastes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value("접근 권한이 없습니다."));
    }
}