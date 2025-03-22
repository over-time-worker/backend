package com.owlexpress.order.application.dto.response;

import com.owlexpress.order.common.constant.CompanyType;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetConsumerInfoResponseDto {
    private String userName;

    private String userPhoneNumber;

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
