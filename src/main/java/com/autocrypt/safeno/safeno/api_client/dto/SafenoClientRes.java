package com.autocrypt.safeno.safeno.api_client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class SafenoClientRes {

    private String telNo;
    private String safeNo;

    // Provider마다 다른 API spec에서 필요한 값이 추가될 여지를 위해서 남겨놓음.
    @Builder.Default
    private Map<String, Object> others = new HashMap<>();
}
