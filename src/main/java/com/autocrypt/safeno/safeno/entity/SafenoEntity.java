package com.autocrypt.safeno.safeno.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "safeno_entity",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_safenoentity_safeno_id", columnNames = {"safeno_id", "boarder_entity_boarder_id"})
        })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SafenoEntity extends AuditMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "safeno_id", nullable = false)
    private Long safenoId;

    @ManyToOne
    @JoinColumn(name = "boarder_entity_boarder_id", foreignKey = @ForeignKey(name = "fk_safeno_boarder"))
    private BoarderEntity boarderEntity;

}