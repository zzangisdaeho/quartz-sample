package com.autocrypt.safeno.safeno.controller.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateExpiredDateReq {

    @NotNull
    @NotEmpty
    private String telNo;

    @NotNull
    @NotEmpty
    private String bookingId;

    @NotNull
    @NotEmpty
    private String expiredDate;
}