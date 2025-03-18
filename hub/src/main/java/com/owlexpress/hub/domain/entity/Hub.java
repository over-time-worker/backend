package com.owlexpress.hub.domain.entity;

import com.owlexpress.hub.common.BaseEntity;
import com.owlexpress.hub.common.util.GeoUtil;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "hub_address", nullable = false)
    private String hubAddress;

    //SRID 좌표계 값. 대표적으로 0(좌표 평면), 4326(위도-경도 좌표계)
    @Column(name = "location", columnDefinition = "GEOMETRY(Point, 4326)", nullable = false)
    private Point location;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "user_phone_number", length = 15)
    private String userPhoneNumber;

    @Column(name = "parent_id")
    private UUID parentHubId;

    @OneToMany(mappedBy = "hub", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<HubProduct> hubProduct = new ArrayList<>();

    @OneToMany(mappedBy = "startHub", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HubIntervalInfo> startIntervals = new ArrayList<>();

    @OneToMany(mappedBy = "endHub", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HubIntervalInfo> endIntervals = new ArrayList<>();

    @Builder
    public Hub(
            UUID hubId,
            String name,
            String hubAddress,
            Point location,
            Long userId,
            String userName,
            String userPhoneNumber,
            UUID parentHubId
    ) {
        this.hubId = hubId;
        this.name = name;
        this.hubAddress = hubAddress;
        this.location = location;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.parentHubId = parentHubId;
    }

    public void update(HubUpdateRequestDto requestDto) {
        // PUT 매핑으로 변경하기 떄문에 모든 값이 다 채워진 상태로 전달됨.
        this.name = requestDto.getName();

        this.hubAddress = requestDto.getHubAddress();

        this.location = GeoUtil.createPoint(
                requestDto.getLatitude(),
                requestDto.getLongitude()
        );

        this.userId = requestDto.getUserId();

        this.userName = requestDto.getUserName();

        this.userPhoneNumber = requestDto.getUserPhoneNumber();

        this.parentHubId = requestDto.getParentId();
    }

}
