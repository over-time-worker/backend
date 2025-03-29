package com.owl_express.alarm.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owl_express.alarm.common.dto.request.AlarmCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {
    private final AlarmService alarmService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "manager.send-alarm-company", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload Object message, Acknowledgment ack) {
        try {
            AlarmCreateRequestDto requestDto
                    = objectMapper.convertValue(message, AlarmCreateRequestDto.class);

            // TODO : 추후에 feign 다 빼게 된다면 Method parameter passport 없애야함
            alarmService.createAlarmForCompanyDeliver(requestDto, null);
            ack.acknowledge();

        } catch (Exception e) {
            log.error("메시지 처리 실패: {}", e.getMessage());
        }
    }
}
