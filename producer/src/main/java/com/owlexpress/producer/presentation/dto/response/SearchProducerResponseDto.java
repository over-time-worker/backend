package com.owlexpress.producer.presentation.dto.response;

import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.common.constant.CompanyType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class SearchProducerResponseDto {
    public UUID hubId;

    public Long hubManagerId;

    private UUID producerId;

    private Long userId;

    private String userName;

    private String userPhoneNumber;

    private String businessNumber;

    private CompanyType companyType;

    private String companyAddress;

    private Double longitude;

    private Double latitude;

    private String hubAddress;

    @Builder
    public SearchProducerResponseDto(
            UUID hubId,
            Long hubManagerId,
            UUID producerId,
            Long userId,
            String userName,
            String userPhoneNumber,
            String businessNumber,
            CompanyType companyType,
            String companyAddress,
            Double longitude,
            Double latitude,
            String hubAddress
    ) {
        this.hubId = hubId;
        this.hubManagerId = hubManagerId;
        this.producerId = producerId;
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.businessNumber = businessNumber;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.hubAddress = hubAddress;
    }

    public static SearchProducerResponseDto fromEntity(Producer producer) {
        return SearchProducerResponseDto.builder()
                                  .hubId(producer.getHubId())
                                  .hubManagerId(producer.getHubManagerId())
                                  .hubAddress(producer.getHubAddress())
                                  .producerId(producer.getProducerId())
                                  .userId(producer.getUserId())
                                  .userName(producer.getUserName())
                                  .userPhoneNumber(producer.getUserPhoneNumber())
                                  .businessNumber(producer.getBusinessNumber())
                                  .companyType(producer.getCompanyType())
                                  .companyAddress(producer.getCompanyAddress())
                                  .latitude(producer.getLocation().getX())
                                  .longitude(producer.getLocation().getY())
                                  .build();
    }
}
