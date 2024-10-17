package com.autocrypt.safe_no.safe_no.service.biz;

import com.autocrypt.safe_no.safe_no.api_client.SafeNoClient;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafeNoClientCaller {

    private final Map<SafeNoProperties.ProviderEnum, SafeNoClient> safeNoClientMap;

    public SafeNoClientRes createSafeNo(SafeNoProperties.ServiceEnum serviceId, String telNo) {
        SafeNoClient safenoClient = safeNoClientMap.get(SafeNoUtil.getProviderEnum(serviceId));
        //safeNo 생성
        return safenoClient.createSafeNo(SafeNoClientReq.builder().telNo(telNo).build());
    }

    public SafeNoClientRes findSafeNo(SafeNoProperties.ServiceEnum serviceId, String safeNo){
        SafeNoClient safenoClient = safeNoClientMap.get(SafeNoUtil.getProviderEnum(serviceId));
        //safeNo 조회
        return safenoClient.readSafeNo(SafeNoClientReq.builder().telNo(safeNo).build());
    }

    public SafeNoClientRes releaseSafeNo(String safeNo, SafeNoProperties.ServiceEnum serviceId) {
        SafeNoClient safenoClient = safeNoClientMap.get(SafeNoUtil.getProviderEnum(serviceId));
        return safenoClient.deleteSafeNo(SafeNoClientReq.builder().safeNo(safeNo).build());
    }
}
