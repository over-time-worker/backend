package com.owlexpress.order.infrastructure.kafka;

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


     //결제 요청
    @Bean
    public NewTopic paymentRequestTopic() {
        return new NewTopic("order.ready", 3, (short) 1);
    }

    //허브 상품 조회 요청
    @Bean
    public NewTopic hubProductRequestTopic(){
        return new NewTopic("order.find-product", 3, (short) 1);
    }


    //소비업체 조회 요청
    @Bean
    public NewTopic consumerReqeustTopic(){
        return new NewTopic("order.find-consumer", 3, (short) 1);
    }

}
