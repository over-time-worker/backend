package com.owlexpress.producer.common.dto.request;

import com.owlexpress.producer.domain.entity.constant.CompanyType;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProducerRequestDto {


    @Size(min = 1, max = 20)
    private String businessNumber;
    @Size(min = 1, max = 20)
    private String companyName;
    private CompanyType companyType;
    @Size(min = 1, max = 255)
    private String companyAddress;
    private Double latitude;
    private Double longitude;
    private UUID hubId;

    @Builder
    public UpdateProducerRequestDto(
            String businessNumber,
            String companyName,
            CompanyType companyType,
            String companyAddress,
            Double latitude,
            Double longitude,
            UUID hubId
    ) {
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubId = hubId;
    }

}
