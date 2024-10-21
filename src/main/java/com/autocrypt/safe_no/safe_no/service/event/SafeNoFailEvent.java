package com.autocrypt.safe_no.safe_no.service.event;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SafeNoFailEvent {

    private String driveId;

    private String telNo;

    private String safeNo;

    private SafeNoProperties.ServiceEnum serviceEnum;
}
