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
    public NewTopic hubDeliveryManagerAssignRequestTopic(){
        return new NewTopic("delivery.hub-assign-manager", 3, (short) 1);
    }
    //업체 배송 담당 매니저 할당 요청
    @Bean
    public NewTopic consumerDeliveryManagerAssignRequestTopic(){
        return new NewTopic("delivery.consumer-assign-manager", 3, (short) 1);
    }

    //허브 배송 담당 매니저 반환 요청
    @Bean
    public NewTopic hubDeliveryManagerReturnRequestTopic(){
        return new NewTopic("delivery.hub-return-manager", 3, (short) 1);
    }
    //업체 배송 담당 매니저 반환 요청
    @Bean
    public NewTopic consumerDeliveryManagerReturnRequestTopic(){
        return new NewTopic("delivery.consumer-return-manager", 3, (short) 1);
    }

    //허브 배송 담당자 배정 취소 알람 요청 (fallback)
    @Bean
    public NewTopic hubDeliveryCancelRequestTopic(){
        return new NewTopic("delivery.error-hub-cancel", 3, (short) 1);
    }

    //업체 배송 담당자 배정 취소 알람 요청 (fallback)
    @Bean
    public NewTopic consumerDeliveryCancelRequestTopic(){
        return new NewTopic("delivery.error-consumer-cancel", 3, (short) 1);
    }

}
