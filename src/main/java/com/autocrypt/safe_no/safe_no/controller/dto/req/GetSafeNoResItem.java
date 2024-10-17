package com.autocrypt.safe_no.safe_no.controller.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSafeNoResItem {

    private String mobileNo;
    private String safeNo;
    private String expiredDate;
}