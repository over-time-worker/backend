package com.owl_express.alarm.infrastructure.kafka;

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


    //ai에게 메세지 전달 요청
    @Bean
    public NewTopic aiRequestTopic() {
        return new NewTopic("alarm.send-ai", 3, (short) 1);
    }

    //성공적으로 ai에게 받은 메세지 전달
    @Bean
    public NewTopic managerSuccessTopic(){
        return new NewTopic("alarm.success", 3, (short) 1);
    }

    //임의로 생성한 메세지 전달
    @Bean
    public NewTopic managerErrorTopic(){
        return new NewTopic("alarm.error", 3, (short) 1);
    }

}
