package com.autocrypt.safe_no.safe_no.entity;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "drive_entity"
//        uniqueConstraints = {@UniqueConstraint(name = "uc_drive_entity_drive_id_service_enum", columnNames = {"drive_id", "service_enum"})}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriveEntity extends AuditMetadata {
    @Id
    @Column(name = "drive_id", nullable = false)
    private String driveId;

    @OneToMany(mappedBy = "driveEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    @Builder.Default
    private List<PassengerEntity> passengerEntities = new ArrayList<>();

//    @Enumerated(EnumType.STRING)
//    @Column(name = "service_enum")
//    private SafeNoProperties.ServiceEnum serviceEnum;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "driveId = " + getDriveId() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ")";
    }

    public PassengerEntity addPassenger(String telNo){
        PassengerEntity newPassenger = PassengerEntity.builder().telNo(telNo).driveEntity(this).build();
        this.passengerEntities.add(newPassenger);

        return newPassenger;
    }

    public List<String> gatherChildSafeNoList() {
        return this.passengerEntities.stream()
                .flatMap(passengerEntity -> passengerEntity.getSafeNoEntities().stream())
                .map(SafeNoEntity::getSafeNo)
                .toList();
    }
}