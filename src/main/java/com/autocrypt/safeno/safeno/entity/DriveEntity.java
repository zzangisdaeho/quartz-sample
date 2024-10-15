package com.autocrypt.safeno.safeno.entity;

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

    @OneToMany(mappedBy = "driveEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    @Builder.Default
    private List<BoarderEntity> boarderEntities = new ArrayList<>();

}