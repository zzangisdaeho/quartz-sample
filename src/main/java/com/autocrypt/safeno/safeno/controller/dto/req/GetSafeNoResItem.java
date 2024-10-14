package com.autocrypt.safeno.safeno.controller.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSafeNoResItem {

    private String mobileNo;
    private String safeNo;
    private String expiredDate;
}