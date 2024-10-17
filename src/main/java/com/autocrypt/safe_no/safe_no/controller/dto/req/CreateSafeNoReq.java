package com.autocrypt.safe_no.safe_no.controller.dto.req;


import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import com.autocrypt.safe_no.safe_no.util.TimeUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CreateSafeNoReq {

    @NotNull
    @NotEmpty
    private String telNo;

    @NotNull
    private SafeNoProperties.ServiceEnum serviceId;

    @NotNull
    @NotEmpty
    private String expiredDate;

    public void setTelNo(@NotNull @NotEmpty String telNo) {
        this.telNo = SafeNoUtil.getTelNoNumberOnly(telNo);
    }

    public void setExpiredDate(ZonedDateTime expiredDate) {
        this.expiredDate =TimeUtil.formatZonedDateTime(TimeUtil.switchZoneToKorea(expiredDate));
    }
}