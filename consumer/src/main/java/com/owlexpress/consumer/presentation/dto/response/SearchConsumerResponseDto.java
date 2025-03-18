package com.owlexpress.consumer.presentation.dto.response;

import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.entity.constant.CompanyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class SearchConsumerResponseDto {

    /**
     * UserInfo
     */
    private Long userId;

    //TODO:: 어차피 조회로 가져오니까 아래 두 필드는 삭제하기
    private String userName;

    private String userPhoneNumber;

    /**
     * Consumer(Entity) Info
     */
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

    /**
     * Hub Info
     */
    private UUID hubId;

    @Builder
    public SearchConsumerResponseDto(
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

    public static SearchConsumerResponseDto fromEntity(Consumer consumer) {
       return SearchConsumerResponseDto.builder()
                                 .userId(consumer.getUserId())
                                 .userName(consumer.getUserName())
                                 .userPhoneNumber(consumer.getUserPhoneNumber())
                                 .companyType(consumer.getCompanyType())
                                 .companyName(consumer.getCompanyName())
                                 .companyAddress(consumer.getCompanyAddress())
                                 .businessNumber(consumer.getBusinessNumber())
                                 .latitude(consumer.getLocation()
                                                   .getX())
                                 .longitude(consumer.getLocation()
                                                    .getY())
                                 .hubId(consumer.getHubId())
                                 .build();
    }
}
