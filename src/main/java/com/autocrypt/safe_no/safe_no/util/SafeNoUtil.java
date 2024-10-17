package com.autocrypt.safe_no.safe_no.util;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SafeNoUtil {

    private static SafeNoProperties safenoProperties;

    public SafeNoUtil(SafeNoProperties safenoProperties) {
        SafeNoUtil.safenoProperties = safenoProperties;
    }

    // 숫자만 남긴 번호
    public static String getTelNoNumberOnly(String telNo) {
        return telNo.replaceAll("[^0-9]", "");
    }

    public static SafeNoProperties.Provider getSafeNoProviderProperty(SafeNoProperties.ServiceEnum serviceId) {

        SafeNoProperties.Provider provider = safenoProperties.getProvider().get(safenoProperties.getService().get(serviceId).getProvider());
        if (provider == null) throw new CustomException("service do not have provider : " + serviceId, HttpStatus.INTERNAL_SERVER_ERROR);
        return provider;
    }

    public static SafeNoProperties.ProviderEnum getProviderEnum(@NotNull SafeNoProperties.ServiceEnum serviceId) {
        return safenoProperties.getService().get(serviceId).getProvider();
    }

    public static SafeNoProperties.Service getServiceProperty(@NotNull SafeNoProperties.ServiceEnum serviceId){
        return safenoProperties.getService().get(serviceId);
    }

}
