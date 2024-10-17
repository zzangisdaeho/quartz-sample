package com.autocrypt.safe_no.safe_no.controller.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteSafeNoReq {

    @NotNull
    @NotEmpty
    private String bookingId;
}