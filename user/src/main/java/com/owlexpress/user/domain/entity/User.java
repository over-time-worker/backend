package com.owlexpress.user.domain.entity;

import com.owlexpress.user.common.BaseEntity;
import com.owlexpress.user.domain.vo.PlatformType;
import com.owlexpress.user.domain.vo.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "platform_id", nullable = false)
    private String platformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", nullable = false)
    private PlatformType platformType;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Builder
    public User(
            String accountId,
            String password,
            String username,
            String phoneNumber,
            String platformId,
            PlatformType platformType,
            Role role,
            Boolean isPublic
    ) {
        this.accountId = accountId;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.isPublic = isPublic;
        super.createdEntity(-1L);
    }

    @Builder
    public void setUserInfo(
            Long userId,
            String PlatformId,
            PlatformType platformType,
            String phoneNumber,
            Boolean isPublic
    ) {
        this.platformId = PlatformId;
        this.platformType = platformType;
        this.phoneNumber = phoneNumber;
        this.isPublic = isPublic;
        super.updateEntity(userId);
    }

    public void setNewPassword(String newPassword, Long userId){
        this.password = newPassword;
        super.updateEntity(userId);
    }

    public void deleteUser(Long userId) {
        super.deleteEntity(userId);
    }
}
