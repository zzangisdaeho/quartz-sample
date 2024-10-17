package com.autocrypt.safe_no.safe_no.controller.dto.res;

import com.autocrypt.safe_no.safe_no.controller.dto.req.GetSafeNoResItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSafeNoRes {

    private String code;
    private GetSafeNoResItem result;
}