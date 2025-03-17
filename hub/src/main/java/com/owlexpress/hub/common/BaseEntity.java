package com.owlexpress.hub.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    //    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    //    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 임시로 생성자 변경
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
