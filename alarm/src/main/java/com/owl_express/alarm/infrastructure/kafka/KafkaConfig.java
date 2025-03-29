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


    //ai에게 허브 배송 담당자용 메세지 생성 요청
    @Bean
    public NewTopic aiHubRequestTopic() {
        return new NewTopic("alarm.send-hub-ai", 3, (short) 1);
    }

    //ai에게 업체 배송 담당자용 메세지 생성 요청
    @Bean
    public NewTopic aiCompanyRequestTopic() {
        return new NewTopic("alarm.send-company-ai", 3, (short) 1);
    }

    //성공적으로 ai에게 받은 허브 배송 담당자용 메세지 전달
    @Bean
    public NewTopic HubAlarmSuccessTopic(){
        return new NewTopic("alarm.success-hub", 3, (short) 1);
    }

    //성공적으로 ai에게 받은 업체 배송 담당자용 메세지 전달
    @Bean
    public NewTopic CompanyAlarmSuccessTopic(){
        return new NewTopic("alarm.success-company", 3, (short) 1);
    }

    //임의로 생성한 메세지 전달
    @Bean
    public NewTopic AlarmErrorTopic(){
        return new NewTopic("alarm.error", 3, (short) 1);
    }

}
