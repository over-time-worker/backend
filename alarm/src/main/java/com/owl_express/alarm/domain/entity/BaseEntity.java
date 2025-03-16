package com.owl_express.alarm.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    // TODO
    //    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    // TODO
    //    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void createdEntity(Long userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    public void modifiedEntity(Long userId){
        this.modifiedBy = userId;
        this.modifiedAt = LocalDateTime.now();
    }

    public void deleteEntity(Long userId) {
        this.deletedBy = userId;
        this.deletedAt = LocalDateTime.now();
    }
}
