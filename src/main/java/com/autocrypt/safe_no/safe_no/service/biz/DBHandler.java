package com.autocrypt.safe_no.safe_no.service.biz;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.config.exception.CustomException;
import com.autocrypt.safe_no.safe_no.config.exception.ExceptionSupplier;
import com.autocrypt.safe_no.safe_no.controller.dto.req.CreateSafeNoReq;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import com.autocrypt.safe_no.safe_no.entity.PassengerEntity;
import com.autocrypt.safe_no.safe_no.entity.SafeNoEntity;
import com.autocrypt.safe_no.safe_no.repository.DriveEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBHandler {

    private final DriveEntityRepository driveEntityRepository;

    @Transactional
    public DriveEntity createDriving(String driveId, CreateSafeNoReq req, SafeNoClientRes createdSafeNo) {
        //Drive를 찾아서 없으면 생성
        DriveEntity driveEntity = driveEntityRepository.findById(driveId)
                .orElseGet(() -> driveEntityRepository.save(DriveEntity.builder()
                        .driveId(driveId)
                        .serviceEnum(req.getServiceId())
                        .build()));

        //Drive 내부에서 telNo로 생성된 passenger가 있는지 확인
        //passenger가 없을시 Drive에 passenger생성
        PassengerEntity findPassenger = driveEntity.getPassengerEntities().stream()
                .filter(boarderEntity -> boarderEntity.getTelNo().equalsIgnoreCase(req.getTelNo()))
                .findFirst().orElseGet(() -> driveEntity.addPassenger(req.getTelNo()));

        //passenger에 safeNo정보 생성
        findPassenger.addSafeNo(createdSafeNo.getSafeNo());

        return driveEntity;
    }

    @Transactional
    public SafeNoEntity findSafeNo(String driveId, String telNo, SafeNoProperties.ServiceEnum serviceId) {
        DriveEntity driveEntity = driveEntityRepository.findByDriveIdAndServiceEnum(driveId, serviceId)
                .orElseThrow(ExceptionSupplier.driveNotFound(driveId));
        PassengerEntity passengerEntity = driveEntity.getPassengerEntities().stream()
                .filter(boarderEntity -> boarderEntity.getTelNo().equalsIgnoreCase(telNo)).findFirst()
                .orElseThrow(ExceptionSupplier.telNoNotFound(telNo, driveId));
        try {
            return passengerEntity.getSafeNoEntities().getLast();
        } catch (NoSuchElementException e) {
            throw new CustomException(String.format("There is no history of a safety number being issued to that telno %s. bookingId: %s", telNo, driveId));
        }

    }

    @Transactional
    public void addSafeNo(PassengerEntity passengerEntity, SafeNoClientRes createdSafeNo) {
        passengerEntity.addSafeNo(createdSafeNo.getSafeNo());
    }

    public DriveEntity deleteFinishedDrive(String driveId) {
        DriveEntity driveEntity = driveEntityRepository.findAllDataByDriveId(driveId).orElseThrow(ExceptionSupplier.driveNotFound(driveId));
        driveEntityRepository.delete(driveEntity);
        return driveEntity;
    }
}
