package com.owlexpress.producer.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "product.search")
public class ProductSearchConfig {
    private int defaultPageSize;
    private List<Integer> allowedPageSizes;
    private String defaultSort;
    private List<String> allowedSorts;
}