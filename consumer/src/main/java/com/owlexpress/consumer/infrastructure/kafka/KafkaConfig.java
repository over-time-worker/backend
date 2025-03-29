package com.owlexpress.consumer.infrastructure.kafka;

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


     //조회 성공시
    @Bean
    public NewTopic orderSuccessTopic(){
        return new NewTopic("consumer.success", 3, (short) 1);
    }

    //조회 실패시
    @Bean
    public NewTopic orderErrorTopic(){
        return new NewTopic("consumer.error", 3, (short) 1);
    }

}
