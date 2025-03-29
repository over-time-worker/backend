package com.owlexpress.deliverymanager.infrastructure.kafka;

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


     //hub 매니저 할당 성공
    @Bean
    public NewTopic hubDeliverySuccessTopic(){
        return new NewTopic("manager.success-hub", 3, (short) 1);
    }

    // company 매니저 할당 성공
    @Bean
    public NewTopic companyDeliverySuccessTopic(){
        return new NewTopic("manager.success-hub", 3, (short) 1);
    }

    //hub 매니저 할당 실패
    @Bean
    public NewTopic hubDeliveryErrorTopic(){
        return new NewTopic("manager.error-hub", 3, (short) 1);
    }

    //company 매니저 할당 실패
    @Bean
    public NewTopic companyDeliveryErrorTopic(){
        return new NewTopic("manager.error-company", 3, (short) 1);
    }

    //허브 배송 담당자용 알람 요청
    @Bean
    public NewTopic hubAlarmRequestTopic(){
        return new NewTopic("manager.send-alarm-hub", 3, (short) 1);
    }

    //업체 배송 담당자용 알람 요청
    @Bean
    public NewTopic companyAlarmRequestTopic(){
        return new NewTopic("manager.send-alarm-company", 3, (short) 1);
    }

    //락 획득 실패시 재시도 요청
    @Bean
    public NewTopic assignLockRetryTopic(){
        return new NewTopic("manager.retry", 3, (short) 1);
    }

}
