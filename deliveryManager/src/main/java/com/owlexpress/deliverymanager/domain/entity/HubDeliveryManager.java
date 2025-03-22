package com.owlexpress.deliverymanager.domain.entity;

import com.owlexpress.deliverymanager.domain.constant.PlatformType;
import com.owlexpress.deliverymanager.infrastructure.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_delivery_manager")
@SQLRestriction("deleted_at is null")
public class HubDeliveryManager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_delivery_manager_id")
    private UUID hubDeliveryManagerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "assign_number")
    private Integer assignNumber;

    @Column(name = "user_name")
    @Size(min = 1, max = 50)
    private String userName;

    @Column(name = "user_phone_number")
    @Size(min = 1, max = 15)
    private String userPhoneNumber;

    @Column(name = "platform_type")
    @Enumerated(EnumType.STRING)
    private PlatformType platformType;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "is_avaliable")
    private Boolean isAvaliable;

    @Builder
    public HubDeliveryManager(
            UUID hubDeliveryManagerId,
            Long userId,
            Integer assignNumber,
            String userName,
            String userPhoneNumber,
            PlatformType platformType,
            String channelId,
            boolean isAvaliable
    ) {
        this.hubDeliveryManagerId = hubDeliveryManagerId;
        this.userId = userId;
        this.assignNumber = assignNumber;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.platformType = platformType;
        this.channelId = channelId;
        this.isAvaliable = isAvaliable;
    }

}
