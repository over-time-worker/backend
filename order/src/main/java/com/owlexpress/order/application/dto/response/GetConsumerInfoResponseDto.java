package com.owlexpress.order.application.dto.response;

import com.owlexpress.order.common.constant.CompanyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetConsumerInfoResponseDto {

    @Column
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Size(min = 1, max = 50)
    private String companyName;

    @Size(min = 1, max = 255)
    private String companyAddress;

    @Size(min = 1, max = 20)
    private String businessNumber;

    private Double latitude;

    private Double longitude;

    private UUID hubId;

    @Builder
    public GetConsumerInfoResponseDto(
            CompanyType companyType,
            String companyName,
            String companyAddress,
            String businessNumber,
            Double latitude,
            Double longitude,
            UUID hubId
    ) {
        this.companyType = companyType;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.businessNumber = businessNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubId = hubId;
    }
}
