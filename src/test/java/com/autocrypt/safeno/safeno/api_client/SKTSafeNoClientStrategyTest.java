package com.autocrypt.safeno.safeno.api_client;

import com.autocrypt.safeno.safeno.api_client.dto.SafenoClientRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "log_trace=on",
        "quartz.enabled=false"
})
class SKTSafeNoClientStrategyTest {

    @Autowired
    private SKTSafeNoClientStrategy sktsafeNoClientStrategy;

    @Test
    public void createTest(){
        SafenoClientRes safeNo = sktsafeNoClientStrategy.createSafeNo("01051011900", Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        System.out.println("safeNo = " + safeNo);
    }

    @Test
    public void readTest(){
//        SafenoClientRes safenoClientRes = sktsafeNoClientStrategy.readSafeNo("050431428302",Collections.EMPTY_MAP, Collections.EMPTY_MAP);
        SafenoClientRes safenoClientRes = sktsafeNoClientStrategy.readSafeNo("01051011900", Map.of("qry_flag", "C"), Collections.EMPTY_MAP);
        System.out.println("safenoClientRes = " + safenoClientRes);
    }
}