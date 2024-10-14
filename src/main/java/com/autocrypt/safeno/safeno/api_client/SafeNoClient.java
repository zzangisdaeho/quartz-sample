package com.autocrypt.safeno.safeno.api_client;

import com.autocrypt.safeno.safeno.api_client.dto.SafenoClientRes;
import jakarta.annotation.Nullable;

import java.util.Map;

public interface SafeNoClient {

    // Create (생성) 메서드
    SafenoClientRes createSafeNo(String telNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> body,
                                 Map<String, Object> headers);

    // Read (조회) 메서드
    SafenoClientRes readSafeNo(String safeNo,
                               Map<String, Object> requestParams,
                               Map<String, Object> headers);

    // Update (수정) 메서드
    SafenoClientRes updateSafeNo(String telNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> body,
                                 Map<String, Object> headers);

    // Delete (삭제) 메서드
    SafenoClientRes deleteSafeNo(String safeNo,
                                 Map<String, Object> requestParams,
                                 Map<String, Object> headers);

}
