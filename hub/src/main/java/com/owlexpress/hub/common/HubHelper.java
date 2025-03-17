package com.owlexpress.hub.common;

import com.owlexpress.hub.common.exception.HubException.HubNotFoundException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import java.util.UUID;

public class HubHelper {

    public static Hub findByHubId(UUID hubId, HubRepository hubRepository) {
        return hubRepository.findByHubId(hubId)
                .orElseThrow(HubNotFoundException::new);
    }
}
