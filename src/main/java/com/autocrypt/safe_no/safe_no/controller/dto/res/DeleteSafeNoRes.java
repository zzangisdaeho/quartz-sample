package com.autocrypt.safe_no.safe_no.controller.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteSafeNoRes {

    private String result;
    private String safeNo;
}