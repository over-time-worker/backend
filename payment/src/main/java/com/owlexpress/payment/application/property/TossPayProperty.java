package com.owlexpress.payment.application.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("toss")
@RequiredArgsConstructor
public class TossPayProperty {

    private final String clientKey;
    private final String secretKey;
    private final String authorizationKey;
}
