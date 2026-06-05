package com.example.lsms.global.config;

import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.repository.LabInfoRepository;
import com.example.lsms.user.domain.User;
import com.example.lsms.user.domain.UserRole;
import com.example.lsms.user.repository.UserRepository;
import com.example.lsms.waste.domain.WasteInfo;
import com.example.lsms.waste.domain.WasteStatus;
import com.example.lsms.waste.domain.WasteType;
import com.example.lsms.waste.repository.WasteInfoRepository;
import com.example.lsms.waste.repository.WasteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LabInfoRepository labInfoRepository;
    private final WasteTypeRepository wasteTypeRepository;
    private final WasteInfoRepository wasteInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = userRepository.save(User.builder()
                .id(1L)
                .userId("admin")
                .password(passwordEncoder.encode("1"))
                .name("시스템관리자")
                .department("안전관리팀")
                .phoneNumber("010-0000-0001")
                .role(UserRole.SYSTEM_ADMIN)
                .build());

        User researcher = userRepository.save(User.builder()
                .id(2L)
                .userId("researcher")
                .password(passwordEncoder.encode("1"))
                .name("연구원")
                .department("화학공학과")
                .phoneNumber("010-0000-0002")
                .role(UserRole.RESEARCHER)
                .build());

        LabInfo lab = labInfoRepository.save(LabInfo.builder()
                .manager(admin)
                .deptId(101L)
                .labName("화학공학 연구실")
                .location("공학관 301호")
                .labType("CHEMICAL")
                .isInspectionTarget("Y")
                .contact("054-000-0000")
                .grade("A")
                .build());

        List<WasteType> types = wasteTypeRepository.saveAll(List.of(
                WasteType.builder().code("EXPERIMENTAL_WASTE_LIQUID").name("실험 폐액").description("실험 과정에서 발생한 액체 폐기물").build(),
                WasteType.builder().code("WASTE_REAGENT").name("폐시약").description("사용하지 않거나 폐기 대상인 시약").build(),
                WasteType.builder().code("WASTE_REAGENT_BOTTLE").name("폐시약병").description("시약을 보관했던 폐용기").build(),
                WasteType.builder().code("MEDICAL_WASTE").name("의료 폐기물").description("연구 과정에서 발생한 의료성 폐기물").build()
        ));

        wasteInfoRepository.save(WasteInfo.builder()
                .wasteName("폐산성 용액")
                .wasteType(types.get(0))
                .generatedLab(lab)
                .storageLocation("폐기물 보관함 A-01")
                .registeredBy(researcher)
                .registeredAt(LocalDateTime.now().minusDays(1))
                .status(WasteStatus.REGISTERED)
                .build());
    }
}
