package com.owlexpress.consumer.domain.entity;

import com.owlexpress.consumer.common.BaseEntity;
import com.owlexpress.consumer.domain.entity.constant.CompanyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "p_consumer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Consumer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "consumer_id")
    private UUID consumerId;

    /**
     * UserInfo
     */
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    @Size(min = 1, max = 50)
    private String userName;

    @Column(name = "user_phone_number")
    @Size(min = 1, max = 15)
    private String userPhoneNumber;

    /**
     *  Consumer(Entity) Info
     */
    @Column
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(name = "company_name")
    @Size(min = 1, max = 50)
    private String companyName;

    @Column(name = "company_address")
    @Size(min = 1, max = 255)
    private String companyAddress;

    @Column(name = "business_number")
    @Size(min = 1, max = 20)
    private String businessNumber;

    @Column(name = "location", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point location;


    /**
     * Hub Info
     */
    @Column(name = "hub_id")
    private UUID hubId;

    @Builder
    public Consumer(
            UUID consumerId,
            Long userId,
            String userName,
            String userPhoneNumber,
            CompanyType companyType,
            String companyName,
            String companyAddress,
            String businessNumber,
            Point location,
            UUID hubId
    ) {
        this.consumerId = consumerId;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.companyType = companyType;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.businessNumber = businessNumber;
        this.location = location;
        this.hubId = hubId;
    }
}
