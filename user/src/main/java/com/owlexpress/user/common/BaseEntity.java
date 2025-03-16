package com.owlexpress.user.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// TODO : @EntityListeners(AuditingEntityListener.class) setting
@MappedSuperclass
@Getter
public abstract class BaseEntity {
//   TODO : @CreatedBy 설정
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

//   TODO : @CreatedDate 설정
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Setter
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected void deleteEntity(Long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

    protected void updateEntity(Long userId) {
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = userId;
    }

    protected void createdEntity(Long userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }
}
