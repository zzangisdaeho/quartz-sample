package com.autocrypt.safe_no.safe_no.entity;

import com.autocrypt.safe_no.safe_no.config.SafeNoProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "safe_no_entity")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafeNoEntity extends AuditMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "safe_no_id", nullable = false)
    private Long safeNoId;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "passenger_tel_no", referencedColumnName = "tel_no"),
            @JoinColumn(name = "passenger_entity_drive_id", referencedColumnName = "drive_id")
    }, foreignKey = @ForeignKey(name = "fk_safeno_boarder_entity"))
    private PassengerEntity passengerEntity;

    @Column(name = "safe_no")
    private String safeNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_enum")
    private SafeNoProperties.ProviderEnum providerEnum;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "safeNoId = " + getSafeNoId() + ", " +
                "passengerEntity = " + getPassengerEntity() + ", " +
                "safeNo = " + getSafeNo() + ", " +
                "providerEnum = " + getProviderEnum() + ", " +
                "createdAt = " + getCreatedAt() + ", " +
                "updatedAt = " + getUpdatedAt() + ")";
    }
}