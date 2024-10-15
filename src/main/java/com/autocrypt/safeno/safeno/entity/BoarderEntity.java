package com.autocrypt.safeno.safeno.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "boarder_entity",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_boarderentity_telno_driveId", columnNames = {"telno", "drive_entity_drive_id"})
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoarderEntity extends AuditMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "boarder_id", nullable = false)
    private Long boarderId;

    @Column(name = "telno")
    private String telno;

    @ManyToOne
    @JoinColumn(name = "drive_entity_drive_id", foreignKey = @ForeignKey(name = "fk_boarder_drive"))
    private DriveEntity driveEntity;

    @OneToMany(mappedBy = "boarderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt")
    @Builder.Default
    private List<SafenoEntity> safenoEntities = new ArrayList<>();

}