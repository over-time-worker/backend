package com.owlexpress.consumer.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.consumer.common.dto.response.PassportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PassportHelper {

    public PassportDto getPassportDto(String passport) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.info("info ={} " , passport);
            return  objectMapper.readValue(passport, PassportDto.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
