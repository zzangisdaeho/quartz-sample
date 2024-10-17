package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.api_client.exception.SKTSafenoError;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SKTSafeNoClientTest {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("sktSafenoClient")
    private SafeNoClient sktSafeNoClient;

    private static String createdSafeno;

    @Test
    @Order(1)
    public void createTest(){
        SafeNoClientRes safeNo = sktSafeNoClient.createSafeNo("01051011900", Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        System.out.println("safeNo = " + safeNo);
        Assertions.assertNotNull(safeNo.getSafeNo());
        createdSafeno = safeNo.getSafeNo();
    }

    @Test
    @Order(2)
    public void readTest(){
        SafeNoClientRes safenoClientRes = sktSafeNoClient.readSafeNo(createdSafeno, Collections.EMPTY_MAP, Collections.EMPTY_MAP);

        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());

        //없는 번호로 조회시 에러
        Assertions.assertThrows(SKTSafenoError.class, () -> sktSafeNoClient.readSafeNo(createdSafeno, Map.of("qry_flag", "C"), Collections.EMPTY_MAP));
    }

    @Test
    @Order(3)
    public void deleteTest(){
        SafeNoClientRes safenoClientRes = sktSafeNoClient.deleteSafeNo(createdSafeno, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());
    }
}