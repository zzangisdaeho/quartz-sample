package com.autocrypt.safeno.safeno.repository;

import com.autocrypt.safeno.safeno.entity.DriveEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DriveEntityRepositoryTest {

    @Autowired
    private DriveEntityRepository driveEntityRepository;

    @Test
    public void saveTest() {

        DriveEntity build = DriveEntity.builder()
                .driveId(UUID.randomUUID().toString())
                .build();

        driveEntityRepository.save(build);
    }

}