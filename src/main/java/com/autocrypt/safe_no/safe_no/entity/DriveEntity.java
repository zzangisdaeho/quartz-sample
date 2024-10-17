package com.autocrypt.safe_no.safe_no.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "drive_entity")
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "driveId = " + getDriveId() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ")";
    }

    public PassengerEntity addBoarder(String telNo){
        PassengerEntity newPassenger = PassengerEntity.builder().telNo(telNo).driveEntity(this).build();
        this.passengerEntities.add(newPassenger);

        return newPassenger;
    }
}