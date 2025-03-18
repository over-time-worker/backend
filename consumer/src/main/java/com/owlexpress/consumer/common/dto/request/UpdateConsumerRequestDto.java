package com.owlexpress.consumer.common.dto.request;

import com.owlexpress.consumer.domain.entity.constant.CompanyType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateConsumerRequestDto {
    /**
     * UserInfo
     */
    private Long userId;

    /**
     *  Consumer(Entity) Info
     */
    @Column
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Size(min = 1, max = 50)
    private String companyName;

    @Size(min =1 , max = 255)
    private String companyAddress;

    @Size(min =1 , max = 20)
    private String businessNumber;

    private Double latitude;

    private Double longitude;

    /**
     * Hub Info
     */
    private UUID hubId;

}
