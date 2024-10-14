package com.autocrypt.safeno.safeno.service;

import com.autocrypt.safeno.safeno.config.exception.CustomException;
import com.autocrypt.safeno.safeno.config.SafenoProperties;
import com.autocrypt.safeno.safeno.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safeno.safeno.controller.dto.res.CreateSafeNoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafeNoService {

    private final SafenoProperties safenoProperties;

    private final ApplicationEventPublisher applicationEventPublisher;

    public CreateSafeNoRes createSafeNo(String telNoNumberOnly, CreateSafeNoReq req) {

        SafenoProperties.Provider safeNoProvider = getSafeNoProvider(req.getServiceId());

        return null;
//        SafeNoProviderService safeNoProviderService = getSafeNoProviderService(safeNoProvider);
//
//        Date limitedDate = req.getExpiredDate() == null
//                ? new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15))
//                : new Date(req.getExpiredDate());
//
//        AddRes addRes = safeNoProviderService.makeSafeNo(
//                safeNoProvider,
//                req.getBookingId(),
//                telNoNumberOnly,
//                limitedDate
//        );
//
//        if (addRes.isSuccess()) {
//            SafeNo newSafeno = SafeNo.builder()
//                    .serviceId(req.getServiceId())
//                    .mobileNo(telNoNumberOnly)
//                    .safeNo(addRes.getSafeNo())
//                    .bookingId(req.getBookingId())
//                    .toBeExpired(limitedDate)
//                    .build();
//            logger.info("Save SafeNo try telNo: " + telNo);
//            safenoRepositoryService.save(newSafeno);
//            logger.info("Save SafeNo fin telNo: " + telNo);
//        }
//
//        return CreateSafeNoRes.builder()
//                .result(addRes.isSuccess() ? "00000000" : "09000100")
//                .createSafeNoResItem(CreateSafeNoResItem.builder()
//                        .result(addRes.getProviderRes().getProviderResCode() != null
//                                ? addRes.getProviderRes().getProviderResCode()
//                                : "HTTP Communication Error")
//                        .mobileNo(telNoNumberOnly)
//                        .safeNo(addRes.getSafeNo())
//                        .newFlag(NewFlag.Y)
//                        .build())
//                .build();
    }

    private SafenoProperties.Provider getSafeNoProvider(SafenoProperties.ServiceEnum serviceId) {

        SafenoProperties.Provider provider = safenoProperties.getProvider().get(serviceId);
        if (provider == null) throw new CustomException("service do not have provider : " + serviceId, HttpStatus.INTERNAL_SERVER_ERROR);
        return provider;
    }
}
