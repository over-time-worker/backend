package com.owlexpress.product.common.dto.response;

import com.owlexpress.product.common.constant.CompanyType;
import com.owlexpress.product.domain.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class ProducerResponseDto {

    public UUID hubId;

    public Long hubManagerId;

    private UUID producerId;

    private Long userId;

    private String userName;

    private String userPhoneNumber;

    private String businessNumber;

    private String companyName;

    private CompanyType companyType;

    private String companyAddress;

    private Double longitude;

    private Double latitude;

    private String hubAddress;

    @Builder
    public ProducerResponseDto(
            UUID hubId,
            Long hubManagerId,
            UUID producerId,
            Long userId,
            String userName,
            String userPhoneNumber,
            String businessNumber,
            String companyName,
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
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.hubAddress = hubAddress;
    }

    public static void updateProduct(
            ProducerResponseDto producerResponseDto,
            Product product
    ) {
        product.setProducerName(producerResponseDto.getUserName());
        product.setProducerId(producerResponseDto.getProducerId());
        product.setProducerAddress(producerResponseDto.getCompanyAddress());
    }
}
