package com.owlexpress.producer.common.dto.request;

import com.owlexpress.producer.domain.entity.constant.CompanyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProductRequestDto {


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
    public UpdateProductRequestDto(
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
