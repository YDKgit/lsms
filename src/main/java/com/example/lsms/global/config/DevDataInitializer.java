package com.example.lsms.global.config;

import com.example.lsms.chemical.domain.CapacityUnit;
import com.example.lsms.chemical.domain.Chemical;
import com.example.lsms.chemical.domain.ChemicalProperty;
import com.example.lsms.chemical.domain.HazardInfo;
import com.example.lsms.chemical.domain.SignalWord;
import com.example.lsms.chemical.repository.ChemicalRepository;
import com.example.lsms.education.domain.EduCategory;
import com.example.lsms.education.domain.EduContent;
import com.example.lsms.education.domain.EduTerm;
import com.example.lsms.education.repository.EduCategoryRepository;
import com.example.lsms.education.repository.EduContentRepository;
import com.example.lsms.education.repository.EduTermRepository;
import com.example.lsms.inspection.domain.Inspection;
import com.example.lsms.inspection.domain.InspectionDetail;
import com.example.lsms.inspection.enums.InspectionMethod;
import com.example.lsms.inspection.enums.InspectionType;
import com.example.lsms.inspection.repository.ChecklistRepository;
import com.example.lsms.inspection.domain.Checklist;
import com.example.lsms.inspection.repository.InspectionRepository;
import com.example.lsms.lab.domain.LabInfo;
import com.example.lsms.lab.domain.LabUserMapping;
import com.example.lsms.lab.domain.FloorPlan;
import com.example.lsms.lab.domain.LabLayout;
import com.example.lsms.lab.repository.FloorPlanRepository;
import com.example.lsms.lab.repository.LabInfoRepository;
import com.example.lsms.lab.repository.LabLayoutRepository;
import com.example.lsms.lab.repository.LabUserMappingRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LabInfoRepository labInfoRepository;
    private final LabUserMappingRepository labUserMappingRepository;
    private final FloorPlanRepository floorPlanRepository;
    private final LabLayoutRepository labLayoutRepository;
    private final WasteTypeRepository wasteTypeRepository;
    private final WasteInfoRepository wasteInfoRepository;
    private final ChecklistRepository checklistRepository;
    private final InspectionRepository inspectionRepository;
    private final ChemicalRepository chemicalRepository;
    private final EduCategoryRepository eduCategoryRepository;
    private final EduTermRepository eduTermRepository;
    private final EduContentRepository eduContentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedCoreData();
        }
        seedEducationData();
    }

    private void seedCoreData() {
        // ─── 사용자 ─────────────────────────────────────────────
        User admin = user("admin",      "1", "홍길동",   "안전관리팀",  "010-1234-0001", UserRole.SYSTEM_ADMIN);
        User labmgr = user("labmanager","1", "김철수",   "화학공학과",  "010-1234-0010", UserRole.LAB_MANAGER);
        User safety = user("safety",    "1", "이영희",   "안전관리팀",  "010-1234-0011", UserRole.SAFETY_MANAGEMENT_TEAM);
        User labsafe = user("labsafety","1", "박지훈",   "생명공학과",  "010-1234-0012", UserRole.LAB_SAFETY_MANAGER);
        User edumgr = user("edumgr",    "1", "최수진",   "교육지원팀",  "010-1234-0013", UserRole.EDUCATION_MANAGER);
        User researcher = user("researcher","1","정민준", "화학공학과",  "010-1234-0002", UserRole.RESEARCHER);
        userRepository.saveAll(List.of(admin, labmgr, safety, labsafe, edumgr, researcher));

        // ─── 연구실 ──────────────────────────────────────────────
        List<LabInfo> labs = labInfoRepository.saveAll(List.of(
                lab(admin,  "제1화학공학 연구실",  "공학관 301호",  "CHEMICAL", "A"),
                lab(labmgr, "제2화학공학 연구실",  "공학관 302호",  "CHEMICAL", "A"),
                lab(labmgr, "제1생명과학 연구실",  "과학관 201호",  "BIO",      "B"),
                lab(admin,  "제1재료공학 연구실",  "공학관 401호",  "GENERAL",  "B"),
                lab(labsafe,"제1환경공학 연구실",  "환경관 101호",  "GENERAL",  "C"),
                lab(admin,  "제1바이오메디컬 연구실","과학관 305호", "BIO",      "A")
        ));

        // ─── 연구실-사용자 매핑 ───────────────────────────────────
        labUserMappingRepository.saveAll(List.of(
                mapping(labs.get(0), admin),        // 제1화학공학 연구실 책임자
                mapping(labs.get(0), researcher),
                mapping(labs.get(0), labsafe),
                mapping(labs.get(1), labmgr),       // 제2화학공학 연구실 책임자
                mapping(labs.get(1), researcher),
                mapping(labs.get(2), labmgr),       // 제1생명과학 연구실 책임자
                mapping(labs.get(2), labsafe),
                mapping(labs.get(2), researcher),
                mapping(labs.get(3), labmgr),
                mapping(labs.get(4), labsafe),
                mapping(labs.get(5), researcher)
        ));

        // ─── 평면도 / 배치도 ─────────────────────────────────────
        String floorPlanUrl = "http://localhost:8080/images/floorplan.png";
        String layoutUrl    = "http://localhost:8080/images/layout.png";

        List<FloorPlan> floorPlans = new java.util.ArrayList<>();
        List<LabLayout> layouts    = new java.util.ArrayList<>();
        for (LabInfo lab : labs) {
            floorPlans.add(FloorPlan.builder()
                    .lab(lab).buildingName(lab.getLocation()).floorLevel(1).filePath(floorPlanUrl).build());
            layouts.add(LabLayout.builder()
                    .lab(lab).filePath(layoutUrl).layoutData(null).build());
        }
        floorPlanRepository.saveAll(floorPlans);
        labLayoutRepository.saveAll(layouts);

        // ─── 폐기물 유형 + 폐기물 ────────────────────────────────
        List<WasteType> types = wasteTypeRepository.saveAll(List.of(
                WasteType.builder().code("EXPERIMENTAL_WASTE_LIQUID").name("실험 폐액").description("실험 과정에서 발생한 액체 폐기물").build(),
                WasteType.builder().code("WASTE_REAGENT").name("폐시약").description("사용하지 않거나 폐기 대상인 시약").build(),
                WasteType.builder().code("WASTE_REAGENT_BOTTLE").name("폐시약병").description("시약을 보관했던 폐용기").build(),
                WasteType.builder().code("MEDICAL_WASTE").name("의료 폐기물").description("연구 과정에서 발생한 의료성 폐기물").build()
        ));
        wasteInfoRepository.saveAll(List.of(
                waste("폐산성 용액",        types.get(0), labs.get(0), "보관함 A-01", researcher,  1, WasteStatus.REGISTERED),
                waste("폐알칼리 용액",       types.get(0), labs.get(0), "보관함 A-02", researcher,  2, WasteStatus.STORED),
                waste("염화용매 폐액",       types.get(0), labs.get(1), "보관함 B-01", admin,        3, WasteStatus.REGISTERED),
                waste("사용 만료 아세톤",    types.get(1), labs.get(1), "시약창고 C-01", researcher, 5, WasteStatus.STORED),
                waste("폐에탄올 시약",       types.get(1), labs.get(2), "냉장고 D-01", researcher,  7, WasteStatus.DISPOSAL_REQUESTED),
                waste("폐시약병(유리)",      types.get(2), labs.get(2), "분리수거 E-01", admin,      10, WasteStatus.REGISTERED),
                waste("폐시약병(플라스틱)",  types.get(2), labs.get(3), "분리수거 E-02", researcher, 12, WasteStatus.STORED),
                waste("손상된 피펫 팁",      types.get(3), labs.get(3), "의료폐기물 F-01", admin,   14, WasteStatus.REGISTERED),
                waste("실험용 멸균 cotton",  types.get(3), labs.get(4), "의료폐기물 F-02", researcher, 20, WasteStatus.DISPOSED),
                waste("중화처리 폐액",       types.get(0), labs.get(4), "보관함 G-01", admin,       25, WasteStatus.REGISTERED),
                waste("유기용제 혼합 폐액",  types.get(0), labs.get(5), "보관함 H-01", researcher, 30, WasteStatus.STORED),
                waste("만료 표준용액",       types.get(1), labs.get(5), "시약창고 H-02", admin,     35, WasteStatus.REGISTERED)
        ));

        // ─── 체크리스트 ──────────────────────────────────────────
        checklistRepository.saveAll(List.of(
                Checklist.builder().category("소방").content("소화기 위치 및 사용 기한 확인").isUse(true).build(),
                Checklist.builder().category("소방").content("옥내소화전 주변 장애물 여부 확인").isUse(true).build(),
                Checklist.builder().category("전기").content("배선 및 콘센트 이상 여부 확인").isUse(true).build(),
                Checklist.builder().category("환기").content("후드 및 환기시설 작동 상태 확인").isUse(true).build()
        ));

        // ─── 점검 ────────────────────────────────────────────────
        Inspection ins1 = Inspection.builder()
                .lab(labs.get(0)).inspector(labsafe)
                .inspectionDate(LocalDate.now().minusDays(20))
                .inspectionType(InspectionType.DAILY)
                .inspectionMethod(InspectionMethod.ONLINE)
                .inspectionGrade(null).build();

        Inspection ins2 = Inspection.builder()
                .lab(labs.get(1)).inspector(safety)
                .inspectionDate(LocalDate.now().minusDays(14))
                .inspectionType(InspectionType.REGULAR)
                .inspectionMethod(InspectionMethod.OFFLINE)
                .inspectionGrade(85.0).build();

        Inspection ins3 = Inspection.builder()
                .lab(labs.get(2)).inspector(labsafe)
                .inspectionDate(LocalDate.now().minusDays(7))
                .inspectionType(InspectionType.DAILY)
                .inspectionMethod(InspectionMethod.ONLINE)
                .inspectionGrade(null).build();

        Inspection ins4 = Inspection.builder()
                .lab(labs.get(3)).inspector(safety)
                .inspectionDate(LocalDate.now().minusDays(3))
                .inspectionType(InspectionType.OCCASIONAL)
                .inspectionMethod(InspectionMethod.OFFLINE)
                .inspectionGrade(72.0).build();

        Inspection ins5 = Inspection.builder()
                .lab(labs.get(4)).inspector(labsafe)
                .inspectionDate(LocalDate.now().minusDays(1))
                .inspectionType(InspectionType.DAILY)
                .inspectionMethod(InspectionMethod.ONLINE)
                .inspectionGrade(null).build();

        Inspection ins6 = Inspection.builder()
                .lab(labs.get(5)).inspector(safety)
                .inspectionDate(LocalDate.now().minusDays(30))
                .inspectionType(InspectionType.REGULAR)
                .inspectionMethod(InspectionMethod.OFFLINE)
                .inspectionGrade(91.0).build();

        Inspection ins7 = Inspection.builder()
                .lab(labs.get(0)).inspector(labsafe)
                .inspectionDate(LocalDate.now().minusDays(45))
                .inspectionType(InspectionType.REGULAR)
                .inspectionMethod(InspectionMethod.OFFLINE)
                .inspectionGrade(78.0).build();

        // ins1 지적사항 (미조치)
        ins1.addDetail(InspectionDetail.builder()
                .issueCategory("소방").problemDescribe("소화기 압력 게이지 불량 — 즉시 교체 필요").build());
        ins1.addDetail(InspectionDetail.builder()
                .issueCategory("전기").problemDescribe("실험대 멀티탭 과부하 사용 확인").build());

        // ins2 지적사항 (일부 조치완료)
        ins2.addDetail(InspectionDetail.builder()
                .issueCategory("소방").problemDescribe("소화기 사용 기한 초과").build());
        ins2.addDetail(InspectionDetail.builder()
                .issueCategory("전기").problemDescribe("콘센트 피복 손상으로 합선 위험")
                .actionResult("피복 테이프 보강 완료").actionDate(LocalDateTime.now().minusDays(10)).build());
        ins2.addDetail(InspectionDetail.builder()
                .issueCategory("화학").problemDescribe("MSDS 미부착 약품 3종 발견").build());

        // ins3 지적사항 (미조치)
        ins3.addDetail(InspectionDetail.builder()
                .issueCategory("환기").problemDescribe("후드 팬 소음 과다 — 베어링 마모 의심").build());
        ins3.addDetail(InspectionDetail.builder()
                .issueCategory("화학").problemDescribe("인화성 물질 별도 보관 캐비닛 미사용").build());

        // ins4 지적사항 (일부 조치완료)
        ins4.addDetail(InspectionDetail.builder()
                .issueCategory("환기").problemDescribe("후드 필터 교체 필요 (교체 주기 2개월 초과)")
                .actionResult("필터 교체 완료").actionDate(LocalDateTime.now().minusDays(1)).build());
        ins4.addDetail(InspectionDetail.builder()
                .issueCategory("안전장비").problemDescribe("개인보호구(장갑, 고글) 수량 부족").build());
        ins4.addDetail(InspectionDetail.builder()
                .issueCategory("정리정돈").problemDescribe("비상구 앞 실험 장비 적재 확인").build());

        // ins5 지적사항 (미조치)
        ins5.addDetail(InspectionDetail.builder()
                .issueCategory("소방").problemDescribe("스프링클러 헤드 주변 물건 적재").build());

        // ins6 지적사항 (전부 조치완료)
        ins6.addDetail(InspectionDetail.builder()
                .issueCategory("전기").problemDescribe("누전차단기 동작 불량")
                .actionResult("누전차단기 교체 완료").actionDate(LocalDateTime.now().minusDays(25)).build());

        // ins7 지적사항 (미조치)
        ins7.addDetail(InspectionDetail.builder()
                .issueCategory("화학").problemDescribe("폐시약 장기 보관 — 처리 요청 필요").build());
        ins7.addDetail(InspectionDetail.builder()
                .issueCategory("안전장비").problemDescribe("세안 설비(Eye Wash) 배수구 막힘").build());

        // ins8: ins3과 같은 날(minusDays(7)), 다른 연구실 → 달력에서 같은 날 2개 표시 테스트용
        Inspection ins8 = Inspection.builder()
                .lab(labs.get(1)).inspector(safety)
                .inspectionDate(LocalDate.now().minusDays(7))
                .inspectionType(InspectionType.OCCASIONAL)
                .inspectionMethod(InspectionMethod.OFFLINE)
                .inspectionGrade(88.0).build();
        ins8.addDetail(InspectionDetail.builder()
                .issueCategory("소방").problemDescribe("비상구 앞 물건 적치 확인").build());

        inspectionRepository.saveAll(List.of(ins1, ins2, ins3, ins4, ins5, ins6, ins7, ins8));

        // ─── 화학물질 ────────────────────────────────────────────
        chemicalRepository.saveAll(List.of(
                chem(labs.get(0), "64-17-5",   "ETH-001",  "에탄올",          "Merck",   500.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H225 인화성 액체", "P210 열·화기로부터 멀리하시오", "GHS02,GHS07",
                        "무색 투명", "알코올향", 7.0, -114.1, 78.4, 13.0, 365.0),
                chem(labs.get(0), "67-64-1",   "ACE-001",  "아세톤",          "Sigma",   250.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H225 인화성 액체", "P210 열·화기로부터 멀리하시오", "GHS02,GHS07",
                        "무색 투명", "특유의 냄새", 7.0, -94.9, 56.1, -17.0, 465.0),
                chem(labs.get(1), "67-56-1",   "MET-001",  "메탄올",          "Daejung", 300.0, "밀리리터", "mL", SignalWord.DANGER,
                        "H225 인화성 액체, H301 삼키면 독성", "P210,P260,P270", "GHS02,GHS06,GHS08",
                        "무색 투명", "약한 알코올향", null, -97.6, 64.7, 11.0, 385.0),
                chem(labs.get(1), "7647-01-0",  "HCL-001",  "염산 (36%)",      "Samchun",   1.0, "리터",    "L",  SignalWord.DANGER,
                        "H290 금속 부식성, H314 피부·눈 손상", "P260,P280,P301", "GHS05,GHS07",
                        "무색 투명 액체", "자극적인 냄새", null, -26.0, 110.0, null, null),
                chem(labs.get(2), "7664-93-9",  "H2S-001",  "황산 (98%)",      "Junsei",  500.0, "밀리리터", "mL", SignalWord.DANGER,
                        "H290 금속 부식성, H314 심한 피부·눈 손상", "P260,P280,P301,P405", "GHS05",
                        "무색 유성 액체", "무취", null, 10.0, 337.0, null, null),
                chem(labs.get(2), "108-88-3",   "TOL-001",  "톨루엔",          "Merck",   200.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H225 인화성 액체, H304 흡인 독성", "P210,P260,P271", "GHS02,GHS07,GHS08",
                        "무색 투명", "특유의 방향족 냄새", null, -95.0, 110.6, 4.0, 480.0),
                chem(labs.get(3), "110-54-3",   "HEX-001",  "헥산",            "Sigma",   100.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H225 인화성 액체, H304 흡인 독성", "P210,P260", "GHS02,GHS07,GHS08",
                        "무색 투명", "석유향", null, -95.0, 69.0, -22.0, 234.0),
                chem(labs.get(4), "1310-73-2",  "NaOH-001", "수산화나트륨",    "Daejung", 200.0, "그램",    "g",  SignalWord.DANGER,
                        "H290 금속 부식성, H314 심한 화상·눈 손상", "P260,P280,P301,P405", "GHS05",
                        "흰색 고체(펠릿)", "무취", 14.0, 318.0, 1388.0, null, null),
                chem(labs.get(0), "75-09-2",    "DCM-001",  "다이클로로메테인", "Merck",   500.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H351 암 유발 의심, H336 졸음/현기증 유발", "P260,P271,P281", "GHS07,GHS08",
                        "무색 투명", "에터향", null, -95.0, 39.6, null, null),
                chem(labs.get(1), "7664-38-2",  "H3P-001",  "인산 (85%)",      "Samchun", 500.0, "밀리리터", "mL", SignalWord.DANGER,
                        "H290 금속 부식성, H314 피부·눈 손상", "P260,P280,P301", "GHS05",
                        "무색 투명 시럽상", "무취", null, 21.0, 158.0, null, null),
                chem(labs.get(3), "71-43-2",    "BEN-001",  "벤젠",            "Sigma",    50.0, "밀리리터", "mL", SignalWord.DANGER,
                        "H225 인화성, H340 유전적 결함 유발, H350 암 유발", "P201,P210,P260", "GHS02,GHS06,GHS08",
                        "무색 투명", "특유의 방향족 냄새", null, 5.5, 80.1, -11.0, 560.0),
                chem(labs.get(4), "7782-50-5",  "CL2-001",  "염소 가스",       "Samchun",   5.0, "리터",    "L",  SignalWord.DANGER,
                        "H270 산화성 가스, H314 피부·눈 손상, H331 흡입 독성", "P220,P260,P271", "GHS03,GHS05,GHS06",
                        "황록색 기체", "자극적인 냄새", null, -101.0, -34.0, null, null),
                chem(labs.get(5), "75-05-8",    "ACN-001",  "아세토니트릴",    "Daejung", 200.0, "밀리리터", "mL", SignalWord.WARNING,
                        "H225 인화성 액체, H302 삼키면 유해", "P210,P260,P270", "GHS02,GHS07",
                        "무색 투명", "에터향", null, -44.0, 81.6, 2.0, 524.0),
                chem(labs.get(5), "7726-95-6",  "BR2-001",  "브롬",            "Sigma",    25.0, "밀리리터", "mL", SignalWord.DANGER,
                        "H290 금속 부식성, H314 심한 피부·눈 손상, H330 흡입 치명적", "P260,P271,P284,P301", "GHS05,GHS06",
                        "적갈색 액체", "자극적이고 독한 냄새", null, -7.2, 58.8, null, null)
        ));
    }

    private void seedEducationData() {
        if (eduCategoryRepository.count() > 0) return;

        EduCategory fire     = eduCategoryRepository.save(new EduCategory(null, "소방안전", "화재 예방 및 소화기 사용 교육"));
        EduCategory chem     = eduCategoryRepository.save(new EduCategory(null, "화학안전", "유해화학물질 취급 교육"));
        EduCategory general  = eduCategoryRepository.save(new EduCategory(null, "일반안전", "연구실 기본 안전 수칙"));

        EduTerm term = eduTermRepository.save(new EduTerm(null, "2026년 1학기",
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 8, 31)));

        EduContent c1 = new EduContent();
        c1.setTitle("소화기 사용법");
        c1.setVideoUrl("http://localhost:8080/videos/safety1.mp4");
        c1.setDescription("연구실 소화기 점검 및 사용 방법을 학습합니다.");
        c1.setRequiredTime(60); c1.setEduCategory(fire); c1.setEduTerm(term);

        EduContent c2 = new EduContent();
        c2.setTitle("유해화학물질 취급 주의사항");
        c2.setVideoUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
        c2.setDescription("MSDS 확인, 보호구 착용, 누출 대응 절차를 학습합니다.");
        c2.setRequiredTime(120); c2.setEduCategory(chem); c2.setEduTerm(term);

        EduContent c3 = new EduContent();
        c3.setTitle("연구실 기본 안전수칙");
        c3.setVideoUrl("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        c3.setDescription("출입, 정리정돈, 비상연락망 등 기본 수칙을 학습합니다.");
        c3.setRequiredTime(90); c3.setEduCategory(general); c3.setEduTerm(term);

        eduContentRepository.saveAll(List.of(c1, c2, c3));
    }

    // ── 헬퍼 메서드 ──────────────────────────────────────────────────

    private User user(String id, String pw, String name, String dept, String phone, UserRole role) {
        return User.builder().userId(id).password(passwordEncoder.encode(pw))
                .name(name).department(dept).phoneNumber(phone).role(role).build();
    }

    private LabInfo lab(User manager, String name, String location, String type, String grade) {
        return LabInfo.builder().manager(manager).labName(name)
                .location(location).labType(type).isInspectionTarget("Y")
                .contact("054-000-0000").grade(grade).build();
    }

    private LabUserMapping mapping(LabInfo lab, User user) {
        return LabUserMapping.builder().lab(lab).user(user).build();
    }

    private WasteInfo waste(String name, WasteType type, LabInfo lab, String loc,
                            User by, int daysAgo, WasteStatus status) {
        return WasteInfo.builder().wasteName(name).wasteType(type).generatedLab(lab)
                .storageLocation(loc).registeredBy(by)
                .registeredAt(LocalDateTime.now().minusDays(daysAgo)).status(status).build();
    }

    private Chemical chem(LabInfo lab, String cas, String cat, String name, String maker,
                          double amount, String unitName, String unitSymbol,
                          SignalWord signal, String hazard, String precaution, String pictogram,
                          String appearance, String odor, Double ph,
                          Double melt, Double boil, Double flash, Double ignition) {
        return Chemical.builder()
                .lab(lab).casNumber(cas).catNumber(cat).name(name).manufacturer(maker)
                .amount(amount)
                .capacityUnit(CapacityUnit.builder().id(null).name(unitName).symbol(unitSymbol).build())
                .hazardInfo(HazardInfo.builder().signalWord(signal)
                        .hazardStatement(hazard).precautionaryStatement(precaution).pictogram(pictogram).build())
                .chemicalProperty(ChemicalProperty.builder()
                        .appearance(appearance).odor(odor).ph(ph)
                        .meltingPoint(melt).boilingPoint(boil).flashPoint(flash).ignitionPoint(ignition).build())
                .build();
    }
}
