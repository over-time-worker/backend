package com.owlexpress.producer.domain.entity;

import com.owlexpress.producer.common.BaseEntity;
import com.owlexpress.producer.domain.entity.constant.CompanyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Entity
@Getter
@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_producer")
public class Producer extends BaseEntity {
    /**
     * 가져와야할 Hub 값
     */

    @Column(name = "hub_id")
    public UUID hubId;
    @Column(name = "hub_manager_id")
    public UUID hubManagerId;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "producer_id")
    private UUID producer_id;
    /**
     * 가져와야할 User(Owner) 값
     */

    @Column(name = "user_name", nullable = false, unique = true)
    @Size(min = 1, max = 50)
    private String userName;
    @Column(name = "user_phone_number")
    @Size(min = 1, max = 15)
    private String userPhoneNumber;
    @Column(name = "business_number")
    @Size(min = 1, max = 20)
    private String businessNumber;
    /**
     * 등록할 Company 값
     */

    @Column(name = "company_name")
    @Size(min = 1, max = 50)
    private String companyName;
    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;
    @Column(name = "company_address")
    @Size(min = 1, max = 255)
    private String companyAddress;
    //SRID는 좌표계를 나타내는 값으로, 자주 사용하는 값은 0(좌표평면)과 4326(위도-경도 좌표계)이다.
    @Column(name = "location", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point location;
    @Column(name = "hub_address")
    @Size(min = 1, max = 255)
    private String hubAddress;

    @Builder
    public Producer(
            String userName,
            String userPhoneNumber,
            String businessNumber,
            String companyName,
            CompanyType companyType,
            String companyAddress,
            Point location,
            UUID hubId,
            UUID hubManagerId,
            String hubAddress
    ) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.location = location;
        this.hubId = hubId;
        this.hubManagerId = hubManagerId;
        this.hubAddress = hubAddress;
    }
}
