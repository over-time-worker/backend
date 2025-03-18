package com.owlexpress.consumer.common.dto.request;

import com.owlexpress.consumer.domain.entity.constant.CompanyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateConsumerRequestDto {

    /**
     * UserInfo
     */
    private Long userId;

    private String userName;

    private String userPhoneNumber;

    /**
     *  Consumer(Entity) Info
     */
    @Column
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    private String companyName;

    private String companyAddress;

    private String businessNumber;

    private Double latitude;

    private Double longitude;

    /**
     * Hub Info
     */
    private UUID hubId;

    @Builder
    public CreateConsumerRequestDto(
            Long userId,
            String userName,
            String userPhoneNumber,
            CompanyType companyType,
            String companyName,
            String companyAddress,
            String businessNumber,
            Double latitude,
            Double longitude,
            UUID hubId
    ) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.companyType = companyType;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.businessNumber = businessNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubId = hubId;
    }
}
