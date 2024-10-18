package com.autocrypt.safe_no.safe_no.repository;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import com.autocrypt.safe_no.safe_no.entity.DriveEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface DriveEntityRepository extends JpaRepository<DriveEntity, String> {


    @Query("""
            select d from DriveEntity d
            left join fetch d.passengerEntities p
            left join fetch p.safeNoEntities s
            where d.driveId = :driveId
            """)
    Optional<DriveEntity> findAllDataByDriveId(@NonNull String driveId);

    Optional<DriveEntity> findByDriveIdAndServiceEnum(@NonNull String driveId, @NonNull SafeNoProperties.ServiceEnum serviceEnum);
}