package com.owl_express.ai.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;


/**
 * 현재 단일 브로커로 작성한 상황
 */
@Configuration
@EnableKafka
public class KafkaConfig {


    @Bean
    public NewTopic HubAiSuccessTopic(){
        return new NewTopic("ai.success-hub", 3, (short) 1);
    }

    @Bean
    public NewTopic CompanyAiSuccessTopic(){
        return new NewTopic("ai.success-company", 3, (short) 1);
    }

    @Bean
    public NewTopic aiErrorTopic(){
        return new NewTopic("ai.error", 3, (short) 1);
    }

}
