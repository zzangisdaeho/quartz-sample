package com.autocrypt.safeno.safeno.controller.dto.res;

import com.autocrypt.safeno.safeno.controller.dto.req.GetSafeNoResItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSafeNoRes {

    private String code;
    private GetSafeNoResItem result;
}