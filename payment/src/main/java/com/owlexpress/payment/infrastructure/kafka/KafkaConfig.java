package com.owlexpress.payment.infrastructure.kafka;

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


     //배송 생성 요청
    @Bean
    public NewTopic deliveryRequestTopic() {
        return new NewTopic("payment.ready", 3, (short) 1);
    }

    //결제 생성 성공
    @Bean
    public NewTopic paymentSuccessTopic(){
        return new NewTopic("payment.success", 3, (short) 1);
    }


    //결제 생성 실패
    @Bean
    public NewTopic paymentErrorTopic(){
        return new NewTopic("payment.error", 3, (short) 1);
    }

    //허브 이동경로 요청
    @Bean
    public NewTopic hubIntervalInfoRequestTopic(){
        return new NewTopic("payment.find-route", 3, (short) 1);
    }

    //재고 롤백
    @Bean
    public NewTopic hubProductRequestTopic(){
        return new NewTopic("stock.rollback", 3, (short) 1);
    }

}
