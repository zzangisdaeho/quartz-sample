package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;

import java.util.Map;

public interface SafeNoClient {

    // Create (생성) 메서드
    SafeNoClientRes createSafeNo(String telNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> body,
                                 Map<String, Object> headers);

    // Read (조회) 메서드
    SafeNoClientRes readSafeNo(String safeNo,
                               Map<String, Object> requestParams,
                               Map<String, Object> headers);

    // Update (수정) 메서드
    SafeNoClientRes updateSafeNo(String telNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> body,
                                 Map<String, Object> headers);

    // Delete (삭제) 메서드
    SafeNoClientRes deleteSafeNo(String safeNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> headers);

    // 각 클라이언트가 자신에 맞는 ProviderEnum을 반환하도록 하는 메서드
    SafeNoProperties.ProviderEnum getProvider();
}
