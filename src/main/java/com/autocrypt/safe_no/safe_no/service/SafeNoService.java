package com.autocrypt.safe_no.safe_no.service;

import com.autocrypt.safe_no.safe_no.api_client.SafeNoClient;
import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safe_no.safe_no.controller.dto.res.CreateSafeNoRes;
import com.autocrypt.safe_no.safe_no.entity.PassengerEntity;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import com.autocrypt.safe_no.safe_no.repository.DriveEntityRepository;
import com.autocrypt.safe_no.safe_no.util.SafeNoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafeNoService {

    private final Map<SafeNoProperties.ProviderEnum, SafeNoClient> safeNoClientMap;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SafeNoProperties safenoProperties;

    private final DriveEntityRepository driveEntityRepository;


    @Transactional
    public CreateSafeNoRes createSafeNo(String bookingId, CreateSafeNoReq req) {

        SafeNoProperties.Provider safeNoProvider = SafeNoUtil.getSafeNoProvider(req.getServiceId());

        SafeNoClient safenoClient = safeNoClientMap.get(safenoProperties.getService().get(req.getServiceId()).getProvider());

        //Drive를 찾아서 없으면 생성
        DriveEntity driveEntity = driveEntityRepository.findById(bookingId)
                .orElseGet(() -> DriveEntity.builder()
                        .driveId(bookingId)
                        .build());

        //Drive 내부에서 telNo로 생성된 boarder가 있는지 확인
        //boarder가 없을시 Drive에 boarder생성
        Optional<PassengerEntity> findBoarderOptional = driveEntity.getPassengerEntities().stream()
                .filter(boarderEntity -> boarderEntity.getTelNo().equalsIgnoreCase(req.getTelNo()))
                .findFirst();

//        if(findBoarderOptional.isEmpty()){
//            driveEntity)
//        }


//        SafeNoClientRes safeNo = safenoClient.createSafeNo(telNoNumberOnly, null, null, null);

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

    private SafeNoProperties.Provider getSafeNoProvider(SafeNoProperties.ServiceEnum serviceId) {

        SafeNoProperties.Provider provider = safenoProperties.getProvider().get(safenoProperties.getService().get(serviceId).getProvider());
        if (provider == null)
            throw new CustomException("service do not have provider : " + serviceId, HttpStatus.INTERNAL_SERVER_ERROR);
        return provider;
    }
}
