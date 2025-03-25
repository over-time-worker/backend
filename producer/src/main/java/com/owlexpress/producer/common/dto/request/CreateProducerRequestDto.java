package com.owlexpress.producer.common.dto.request;

import com.owlexpress.producer.common.dto.response.GetUserInfoResponseDto;
import com.owlexpress.producer.common.util.GeoUtil;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.common.constant.CompanyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateProducerRequestDto {
    @NotNull(message = "유저 ID값은 필수입니다.")
    private Long userId;
    @Size(min = 1, max = 20)
    @NotNull(message = "사업자 번호는 필수값입니다.")
    private String businessNumber;
    @Size(min = 1, max = 20)
    @NotNull(message = "회사명은 필수값입니다.")
    private String companyName;
    @NotNull(message = "회사 타입값은 필수값입니다.")
    private CompanyType companyType;
    @Size(min = 1, max = 255)
    @NotNull(message = "회사 주소값은 필수값입니다.")
    private String companyAddress;
    @NotNull(message = "위도 값은 필수입니다.")
    private Double latitude;
    @NotNull(message = "경도 값은 필수입니다.")
    private Double longitude;
    @NotNull(message = "허브Id값은 필수값입니다.")
    private UUID hubId;

    @Builder
    public CreateProducerRequestDto(
            Long userId,
            String businessNumber,
            String companyName,
            CompanyType companyType,
            String companyAddress,
            Double latitude,
            Double longitude,
            UUID hubId
    ) {
        this.userId = userId;
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubId = hubId;
    }

    public static Producer toEntity(CreateProducerRequestDto createProducerRequestDto,
                                    GetUserInfoResponseDto UserInfoResponseDto
    ) {

        return Producer.builder()
                .businessNumber(createProducerRequestDto.getBusinessNumber())
                .companyName(createProducerRequestDto.getCompanyName())
                .companyType(createProducerRequestDto.getCompanyType())
                .companyAddress(createProducerRequestDto.getCompanyAddress())
                .userId(createProducerRequestDto.getUserId())
                .userName(UserInfoResponseDto.getUsername())
                .userPhoneNumber(UserInfoResponseDto.getPhoneNumber())
                .hubId(createProducerRequestDto.getHubId())
                .hubManagerId(1L)
                .hubAddress("client에서 가져올 주소")
                .location(GeoUtil.createPoint(
                        createProducerRequestDto.getLatitude(),
                        createProducerRequestDto.getLongitude()
                ))
                .build();

    }
}
