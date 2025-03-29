package com.owlexpress.hub.infrastructure.kafka;

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


     //허브 상품 조회 성공
    @Bean
    public NewTopic orderSuccessTopic(){
        return new NewTopic("hub.success", 3, (short) 1);
    }

    //허브 상품 조회 실패
    @Bean
    public NewTopic orderErrorTopic(){
        return new NewTopic("hub.error", 3, (short) 1);
    }


    //허브 락 획득 재시도
    @Bean
    public NewTopic hubProductStockLockRetryTopic(){
        return new NewTopic("hub.retry", 3, (short) 1);
    }

    //락 획득 실패시 재시도 요청
    @Bean
    public NewTopic assingnRetryTopic(){
        return new NewTopic("manager.retry", 3, (short) 1);
    }

    //허브 이동거리 조회 성공
    @Bean
    public NewTopic paymentSuccessTopic(){
        return new NewTopic("hub-interval-info.success", 3, (short) 1);
    }

    //허브 이동거리 조회 실패
    @Bean
    public NewTopic paymentErrorTopic(){
        return new NewTopic("manager.retry", 3, (short) 1);
    }



}
