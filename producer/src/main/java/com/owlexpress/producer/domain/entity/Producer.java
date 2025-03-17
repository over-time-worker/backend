package com.owlexpress.producer.domain.entity;

import com.owlexpress.producer.common.BaseEntity;
import com.owlexpress.producer.domain.entity.constant.CompanyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_producer")
@SQLRestriction("deleted_at is null")
public class Producer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "producer_id")
    private UUID producerId;

    /**
     * 가져와야할 Hub 값
     */

    @Column(name = "hub_id")
    public UUID hubId;
    @Column(name = "hub_manager_id")
    public Long hubManagerId;
    /**
     * 가져와야할 User(Owner) 값
     */

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
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

    @OneToMany(mappedBy = "producer")
    private List<ProductInfo> productInfos;

    @Builder
    public Producer(
            Long userId,
            String userName,
            String userPhoneNumber,
            String businessNumber,
            String companyName,
            CompanyType companyType,
            String companyAddress,
            Point location,
            UUID hubId,
            Long hubManagerId,
            String hubAddress,
            List<ProductInfo> productInfos
    ) {
        this.userId = userId;
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
        this.productInfos = productInfos;
    }
}
