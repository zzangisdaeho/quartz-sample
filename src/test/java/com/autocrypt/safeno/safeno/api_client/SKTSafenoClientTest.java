package com.autocrypt.safeno.safeno.api_client;

import com.autocrypt.safeno.safeno.api_client.dto.SafenoClientRes;
import com.autocrypt.safeno.safeno.api_client.exception.SKTSafenoError;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SKTSafenoClientTest {

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("sktSafenoClient")
    private SafenoClient sktSafenoClient;

    private static String createdSafeno;

    @Test
    @Order(1)
    public void createTest(){
        SafenoClientRes safeNo = sktSafenoClient.createSafeNo("01051011900", Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        System.out.println("safeNo = " + safeNo);
        Assertions.assertNotNull(safeNo.getSafeNo());
        createdSafeno = safeNo.getSafeNo();
    }

    @Test
    @Order(2)
    public void readTest(){
        SafenoClientRes safenoClientRes = sktSafenoClient.readSafeNo(createdSafeno, Collections.EMPTY_MAP, Collections.EMPTY_MAP);

        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());

        //없는 번호로 조회시 에러
        Assertions.assertThrows(SKTSafenoError.class, () -> sktSafenoClient.readSafeNo(createdSafeno, Map.of("qry_flag", "C"), Collections.EMPTY_MAP));
    }

    @Test
    @Order(3)
    public void deleteTest(){
        SafenoClientRes safenoClientRes = sktSafenoClient.deleteSafeNo(createdSafeno, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        System.out.println("safenoClientRes = " + safenoClientRes);
        Assertions.assertNotNull(safenoClientRes.getSafeNo());
    }
}