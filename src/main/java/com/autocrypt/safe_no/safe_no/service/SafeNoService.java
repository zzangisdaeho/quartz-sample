package com.autocrypt.safe_no.safe_no.service;

import com.autocrypt.safe_no.safe_no.api_client.dto.SafeNoClientRes;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.controller.dto.res.CreateSafeNoRes;
import com.autocrypt.safe_no.safe_no.controller.dto.res.GetSafeNoRes;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import com.autocrypt.safe_no.safe_no.entity.SafeNoEntity;
import com.autocrypt.safe_no.safe_no.service.biz.DBHandler;
import com.autocrypt.safe_no.safe_no.service.biz.SafeNoClientCaller;
import com.autocrypt.safe_no.safe_no.service.biz.SchedulerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafeNoService {

    private final DBHandler dbHandler;
    private final SafeNoClientCaller safeNoClientCaller;
    private final SchedulerHandler schedulerHandler;

    @Transactional
    public CreateSafeNoRes createSafeNo(String driveId, String telNo, SafeNoProperties.ServiceEnum serviceId) {

        //1. SafeNo 생성.
        SafeNoClientRes createdSafeNo = safeNoClientCaller.createSafeNo(serviceId, telNo);

        //2. DB에 기록
        DriveEntity driving = dbHandler.createDriving(driveId, telNo, createdSafeNo);

        //3. Scheduler 등록
        schedulerHandler.registerScheduler(driving.getDriveId(), serviceId);

        return CreateSafeNoRes.builder().safeNo(createdSafeNo.getSafeNo()).build();
    }


    @Transactional
    public GetSafeNoRes getSafeNo(String driveId, String telNo, SafeNoProperties.ServiceEnum serviceId) {
        //1. DB에서 driveId와 telNo를 기준으로 발급된 가장 최신의 safeNo를 가져옴
        SafeNoEntity findSafeNo = dbHandler.findSafeNo(driveId, telNo);

        //2. Provider API를 통해 안심번호가 telNo로 매핑되어있는지 확인
        SafeNoClientRes getSafeNo = safeNoClientCaller.findSafeNo(serviceId, findSafeNo.getSafeNo());

        //2-1 매핑이 잘못되어 있다면 새로 발급 후 passenger에 새로 발급받은것을 등록
        if (!getSafeNo.getTelNo().equals(telNo)) {
            //2-1-1 새로운 안심번호 발급
            SafeNoClientRes createdSafeNo = safeNoClientCaller.createSafeNo(serviceId, telNo);
            //2-1-2 DB에 기록.
            dbHandler.addSafeNo(findSafeNo.getPassengerEntity(), createdSafeNo);
            //2-1-3 새로 발급 받은 번호를 반환
            return GetSafeNoRes.builder().safeNo(createdSafeNo.getSafeNo()).build();
        }
        //2-2 매핑이 재대로 되어있다면 반환
        return GetSafeNoRes.builder().safeNo(getSafeNo.getSafeNo()).build();
    }

    public void updateSafeNoDeleteTime(String driveId){
        // 1. quartz delete job timer를 호출시점으로부터 설정값만큼 뒤의 미래로 업데이트함
        schedulerHandler.delaySchedule(driveId);
    }

    //scheduler에 의해 실행.
    @Transactional
    public void deleteFinishedDrive(String driveId, String serviceId) {
        //1. DB에서 주행이 끝난 데이터 삭제.
        DriveEntity driveEntity = dbHandler.deleteFinishedDrive(driveId);
        //2. drive에 연관된 안심번호를 삭제.
        List<String> safeNoList = driveEntity.gatherChildSafeNoList();
        log.debug("delete targets = size : {}, safeNoList : {}", safeNoList.size(), safeNoList);
        safeNoList.forEach(safeNo -> {
            try{
                SafeNoClientRes safeNoClientRes = safeNoClientCaller.releaseSafeNo(safeNo, SafeNoProperties.ServiceEnum.from(serviceId));
                log.debug("delete safeNo success. driveId: {}, safeNo : {}",driveId, safeNo);
            }catch (Exception e){
                log.error("delete safeNo failed. driveId: {}, safeNo : {}",driveId, safeNo, e);
            }
        });
    }

}
