package com.autocrypt.safe_no.safe_no.repository;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.entity.PassengerEntity;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import com.autocrypt.safe_no.safe_no.entity.SafeNoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DriveEntityRepositoryTest {

    @Autowired
    private DriveEntityRepository driveEntityRepository;

    @Test
    public void saveTest() {

        DriveEntity driveEntity = DriveEntity.builder()
                .driveId(UUID.randomUUID().toString())
                .build();

        PassengerEntity passengerEntity = PassengerEntity.builder()
                .telNo("01051011900")
                .driveEntity(driveEntity)
                .build();

        SafeNoEntity safenoEntity = SafeNoEntity.builder()
                .passengerEntity(passengerEntity)
                .safeNo("05012345678")
                .providerEnum(SafeNoProperties.ProviderEnum.SKT)
                .build();

        driveEntity.getPassengerEntities().add(passengerEntity);
        passengerEntity.getSafeNoEntities().add(safenoEntity);


        DriveEntity save = driveEntityRepository.save(driveEntity);

        System.out.println("save = " + save);
    }

    @Test
    public void readTest(){
        Optional<DriveEntity> byId = driveEntityRepository.findById(UUID.randomUUID().toString());
        assertTrue(byId.isEmpty());
    }

    @Test
    public void deleteTest(){
        driveEntityRepository.deleteById(UUID.randomUUID().toString());
    }


}