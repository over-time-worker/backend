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


     //할당 성공
    @Bean
    public NewTopic deliverySuccessTopic(){
        return new NewTopic("manager.success", 3, (short) 1);
    }

    //할당 실패
    @Bean
    public NewTopic deliveryErrorTopic(){
        return new NewTopic("manager.error", 3, (short) 1);
    }


    //알람 요청
    @Bean
    public NewTopic alarmRequestTopic(){
        return new NewTopic("manager.send-alarm", 3, (short) 1);
    }

    //락 획득 실패시 재시도 요청
    @Bean
    public NewTopic assingLockRetryTopic(){
        return new NewTopic("manager.retry", 3, (short) 1);
    }



}
