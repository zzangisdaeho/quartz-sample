package com.autocrypt.safe_no.safe_no.controller;

import com.autocrypt.safe_no.quartz.jobs.SampleSimpleJob;
import com.autocrypt.safe_no.quartz.service.QuartzSchedulingEventListener;
import com.autocrypt.safe_no.safe_no.api_client.SafeNoClient;
import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.entity.PassengerEntity;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import com.autocrypt.safe_no.safe_no.entity.SafeNoEntity;
import com.autocrypt.safe_no.safe_no.repository.DriveEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final SafeNoProperties safenoProperties;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final Map<SafeNoProperties.ProviderEnum, SafeNoClient> safeNoClientMap;

    private final DriveEntityRepository driveEntityRepository;

    @GetMapping("ping")
    public void ping(){
        System.out.println();
        System.out.println(safenoProperties.toString());
    }

    @GetMapping("/test")
    @Transactional
    public void test() {
        System.out.println(safenoProperties.toString());


        DriveEntity driveEntity = DriveEntity.builder()
                .driveId(UUID.randomUUID().toString())
                .serviceEnum(SafeNoProperties.ServiceEnum.KCALL)
                .build();

        PassengerEntity passengerEntity = PassengerEntity.builder()
                .telNo("01051011900")
                .driveEntity(driveEntity)
                .build();

        SafeNoEntity safeNoEntity1 = SafeNoEntity.builder()
                .passengerEntity(passengerEntity)
                .safeNo("05012345678")
                .build();

        SafeNoEntity safeNoEntity2 = SafeNoEntity.builder()
                .passengerEntity(passengerEntity)
                .safeNo("05012345679")
                .build();

        driveEntity.getPassengerEntities().add(passengerEntity);
        passengerEntity.getSafeNoEntities().add(safeNoEntity1);
        passengerEntity.getSafeNoEntities().add(safeNoEntity2);


        DriveEntity save = driveEntityRepository.save(driveEntity);

        System.out.println("save = " + save);
    }

    @GetMapping("/test/{id}")
    @Transactional
    public void test2(@PathVariable String id){
        Optional<DriveEntity> byId = driveEntityRepository.findById(id);
        System.out.println("byId = " + byId);
    }

    @GetMapping("/event")
    @Transactional
    public String testEvent() {
        applicationEventPublisher.publishEvent(
                QuartzSchedulingEventListener.QuartzJobCreateEvent.builder()
                        .startAt(ZonedDateTime.now().plusSeconds(10))
                        .jobName("testJob1")
                        .jobGroup("testGroup")
                        .jobClass(SampleSimpleJob.class)
                        .build()

        );
        return "갔니?";
    }
}
