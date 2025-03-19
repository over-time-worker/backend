package com.owlexpress.hub.application;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubDistanceInitializer {

    private final HubDistanceService hubDistanceService;
//서버에서 한 번만실행되는 TaskChecker Entity가 필요할지도...?
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        hubDistanceService.calculateAllHubDistances();
    }
}