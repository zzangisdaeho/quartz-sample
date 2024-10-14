package com.autocrypt.safeno.safeno.controller.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateExpiredDateRes {

    private String safeNo;
    private String expiredDate;
}