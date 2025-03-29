package com.owlexpress.delivery.infrastructure.kafka;

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


     //배송 생성 성공
    @Bean
    public NewTopic paymentSuccessTopic(){
        return new NewTopic("delivery.success", 3, (short) 1);
    }

    //배송 생성 실패
    @Bean
    public NewTopic paymentErrorTopic(){
        return new NewTopic("delivery.error", 3, (short) 1);
    }


    //재고 롤백 요청
    @Bean
    public NewTopic hubProductRequestTopic(){
        return new NewTopic("stock.rollback", 3, (short) 1);
    }

    //허브 배송 담당 매니저 할당 요청
    @Bean
    public NewTopic hubDeliveryManagerRequestTopic(){
        return new NewTopic("delivery.hub-assign-manager", 3, (short) 1);
    }
    //업체 배송 담당 매니저 할당 요청
    @Bean
    public NewTopic consumerDeliveryManagerRequestTopic(){
        return new NewTopic("delivery.consumer-assign-manager", 3, (short) 1);
    }



}
