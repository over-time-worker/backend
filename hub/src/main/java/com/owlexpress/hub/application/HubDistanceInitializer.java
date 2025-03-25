package com.owlexpress.hub.application;

import com.owlexpress.hub.domain.service.HubDistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubDistanceInitializer {

    private final HubDistanceService hubDistanceServiceImpl;

    //TODO:: 컨버터 해제하고 직접 코드 작성으로 수정해봄 잘 돌아나가 확인 필수!
    // 이유 = 컨버터 인풋시에는 Long으로 인풋하나 반환시에는 다른타입으로 받아야함 (Entity가 Duration이므로)
//    @EventListener(ApplicationReadyEvent.class)
//    public void initialize() {
//        hubDistanceService.calculateAllHubDistances();
//    }
}