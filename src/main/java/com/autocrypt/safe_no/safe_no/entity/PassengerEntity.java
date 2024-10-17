package com.autocrypt.safe_no.safe_no.entity;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "passenger_entity",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_passenger_entity_tel_no_drive_id", columnNames = {"tel_no", "drive_id"})
        }
)
@IdClass(PassengerEntity.PassengerId.class) // 복합키로 사용할 클래스 지정
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerEntity extends AuditMetadata {

    @Id
    @Column(name = "tel_no")
    private String telNo;

    @Id
    @ManyToOne
    @JoinColumn(name = "drive_id", foreignKey = @ForeignKey(name = "fk_boarder_drive"))
    private DriveEntity driveEntity;

    @OneToMany(mappedBy = "passengerEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderColumn(name = "safe_no_order")
    private List<SafeNoEntity> safeNoEntities = new ArrayList<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "telNo = " + getTelNo() + ", " +
                "driveEntity = " + getDriveEntity() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ")";
    }

    public SafeNoEntity addSafeNo(String safeNo) {
        SafeNoEntity newSafeNo = SafeNoEntity.builder()
                .safeNo(safeNo)
                .passengerEntity(this)
                .build();
        this.safeNoEntities.add(newSafeNo);

        return newSafeNo;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PassengerId implements Serializable {

        private String telNo;
        private String driveEntity; // 복합키를 사용한 엔티티의 PK 필드 이름과 일치해야 합니다
    }
}