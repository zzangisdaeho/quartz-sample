package com.autocrypt.safeno.safeno.controller.dto.req;


import com.autocrypt.safeno.safeno.config.SafenoProperties;
import com.autocrypt.safeno.safeno.util.SafeNoUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSafeNoReq {

    @NotNull
    @NotEmpty
    private String bookingId;

    @NotNull
    @NotEmpty
    private SafenoProperties.ServiceEnum serviceId;

    private String expiredDate;

}