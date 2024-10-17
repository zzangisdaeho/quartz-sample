package com.autocrypt.safe_no.safe_no.api_client;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientReq;
import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;

import java.util.Map;

public interface SafeNoClient {

    // 외부에서 호출 가능한 템플릿 메서드들
    SafeNoClientRes createSafeNo(SafeNoClientReq req);

    SafeNoClientRes readSafeNo(SafeNoClientReq req);

    SafeNoClientRes updateSafeNo(SafeNoClientReq req);

    SafeNoClientRes deleteSafeNo(SafeNoClientReq req);

    // 전략 패턴을 위한 추상 메서드들 (각 클라이언트가 구현)
    Map<String, Object> makeRequestParam(SafeNoClientReq req);
    Map<String, Object> makeHeader(SafeNoClientReq req);
    Map<String, Object> makeBody(SafeNoClientReq req);

    // 각 클라이언트가 자신에 맞는 ProviderEnum을 반환
    SafeNoProperties.ProviderEnum getProvider();
}
