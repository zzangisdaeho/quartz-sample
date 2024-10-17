package com.autocrypt.safe_no.safe_no.controller.dto.req;


import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSafeNoReq {

    @NotNull
    @NotEmpty
    private String telNo;

    @NotNull
    private SafeNoProperties.ServiceEnum serviceId;

    public void setTelNo(@NotNull @NotEmpty String telNo) {
        this.telNo = SafeNoUtil.getTelNoNumberOnly(telNo);
    }

}