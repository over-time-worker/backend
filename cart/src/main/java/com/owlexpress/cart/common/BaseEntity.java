package com.owlexpress.cart.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    //    TODO :: AuditAware 개발 ->  @CreatedBy적용
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    //   TODO :: AuditAware 개발 ->  @CreatedDate적용
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updateCreateData(Long userId) {
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    public void updateModifiedData(Long userId) {
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = userId;
    }

    public void softDeleteData(Long userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}
