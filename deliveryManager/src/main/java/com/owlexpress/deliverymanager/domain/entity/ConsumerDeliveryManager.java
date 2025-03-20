package com.owlexpress.deliverymanager.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_consumer_delivery_manager")
@SQLRestriction("deleted_at is null")
public class ConsumerDeliveryManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_delivery_manager_id")
    private UUID consumerDeliveryManagerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    @Size(min = 1, max = 50)
    private String userName;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "user_phone_number")
    @Size(min = 1, max = 15)
    private String userPhoneNumber;

    @Column(name = "assign_number")
    private Long assignNumber;

    @Column(name = "platform_type")
    private String platformType;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "is_avaliable")
    private Boolean isAvaliable;

    @Builder
    public ConsumerDeliveryManager(
            UUID consumerDeliveryManagerId,
            Long userId,
            Long assignNumber,
            String userName,
            String userPhoneNumber,
            String platformType,
            Long channelId,
            Boolean isAvaliable
    ) {
        this.consumerDeliveryManagerId = consumerDeliveryManagerId;
        this.userId = userId;
        this.assignNumber = assignNumber;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.platformType = platformType;
        this.channelId = channelId;
        this.isAvaliable = isAvaliable;
    }
}
