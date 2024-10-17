package com.autocrypt.safe_no.safe_no.controller.dto.req;

import com.autocrypt.safe_no.safe_no.util.TimeUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

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

    public void setExpiredDate(ZonedDateTime expiredDate) {
        this.expiredDate = TimeUtil.formatZonedDateTime(TimeUtil.switchZoneToKorea(expiredDate));
    }
}