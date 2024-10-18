package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.api_client.exception.SKTSafeNoError;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SejongSafeNoClientTest {

    @Autowired
    @Qualifier("sejongSafeNoClient")
    private SafeNoClient sejongSafeNoClient;

    private static String createdSafeNo;

    @Test
    @DisplayName("안심번호 생성")
    @Order(1)
    public void createTest() {
        SafeNoClientRes safeNo = sejongSafeNoClient.createSafeNo(
                SafeNoClientReq.builder()
                        .telNo("01051011900")
                        .serviceId(SafeNoProperties.ServiceEnum.IMOM)
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
        SafeNoClientRes safenoClientRes = sejongSafeNoClient.readSafeNo(
                SafeNoClientReq.builder()
                        .safeNo(createdSafeNo)
//                        .safeNo("050369828709")
                        .serviceId(SafeNoProperties.ServiceEnum.IMOM)
                        .build());

        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertTrue(StringUtils.isNotBlank(safenoClientRes.getTelNo()));
    }

    @Test
    @DisplayName("안심번호 삭제")
    @Order(3)
    public void deleteTest() {
        SafeNoClientRes safenoClientRes = sejongSafeNoClient.deleteSafeNo(SafeNoClientReq.builder()
                .safeNo(createdSafeNo)
                .serviceId(SafeNoProperties.ServiceEnum.IMOM)
                .build());
        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getOthers().get("code"));
    }
}