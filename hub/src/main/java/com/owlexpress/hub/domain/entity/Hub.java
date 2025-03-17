package com.owlexpress.hub.domain.entity;

import com.owlexpress.hub.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Hub(UUID hubId, String name, String hubAddress, Point location, Long userId,
            String userName,
            String userPhoneNumber, UUID parentHubId) {
        this.hubId = hubId;
        this.name = name;
        this.hubAddress = hubAddress;
        this.location = location;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.parentHubId = parentHubId;
    }
}
