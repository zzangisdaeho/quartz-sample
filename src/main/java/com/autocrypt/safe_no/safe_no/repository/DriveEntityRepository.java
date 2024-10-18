package com.autocrypt.safe_no.safe_no.repository;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface DriveEntityRepository extends JpaRepository<DriveEntity, String> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DriveEntity> findDriveWithLockDriveByDriveId(String driveId);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    Optional<DriveEntity> findDriveWithLockByDriveIdAndServiceEnum(@NonNull String driveId, @NonNull SafeNoProperties.ServiceEnum serviceEnum);
}