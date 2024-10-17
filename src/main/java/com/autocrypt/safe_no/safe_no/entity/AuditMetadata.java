package com.autocrypt.safe_no.safe_no.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public class AuditMetadata {

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
