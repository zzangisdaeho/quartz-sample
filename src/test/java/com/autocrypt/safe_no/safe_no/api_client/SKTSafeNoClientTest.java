package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.api_client.exception.SKTSafeNoError;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SKTSafeNoClientTest {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("sktSafeNoClient")
    private SafeNoClient sktSafeNoClient;

    private static String createdSafeNo;

    @Test
    @DisplayName("안심번호 생성")
    @Order(1)
    public void createTest() {
        SafeNoClientRes safeNo = sktSafeNoClient.createSafeNo(
                SafeNoClientReq.builder()
                        .telNo("01051011900")
                        .build()
        );
        System.out.println("safeNo = " + safeNo);
        Assertions.assertNotNull(safeNo.getSafeNo());
        createdSafeNo = safeNo.getSafeNo();
    }

    @Test
    @DisplayName("안심번호 조회")
    @Order(2)
    public void readTest() {
        SafeNoClientRes safenoClientRes = sktSafeNoClient.readSafeNo(
                SafeNoClientReq.builder()
                        .safeNo(createdSafeNo)
                        .build());

        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());

        //없는 번호로 조회시 에러
        Assertions.assertThrows(SKTSafeNoError.class, () -> {
            SafeNoClientRes safeNoClientRes = sktSafeNoClient.readSafeNo(
                    SafeNoClientReq.builder()
                            .safeNo(createdSafeNo)
                            .others(Map.of("qry_flag", "C"))
                            .build());
        });
    }

    @Test
    @DisplayName("안심번호 삭제")
    @Order(3)
    public void deleteTest() {
        SafeNoClientRes safenoClientRes = sktSafeNoClient.deleteSafeNo(SafeNoClientReq.builder()
                .safeNo(createdSafeNo)
                .build());
        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());
    }
}