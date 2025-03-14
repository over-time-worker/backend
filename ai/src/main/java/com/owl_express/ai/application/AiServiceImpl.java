package com.owl_express.ai.application;

import com.owl_express.ai.application.dtos.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.MessageFindResponseDto;
import com.owl_express.ai.application.exceptions.AiException;
import com.owl_express.ai.application.exceptions.AiException.MessageNotFoundException;
import com.owl_express.ai.domain.entity.Ai;
import com.owl_express.ai.domain.repository.AiRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService{

    private final AiRepository aiRepository;
    private final WebClient webClient;

    @Value("${api.ai.key}")
    private String apiKey;

    @Value("${api.ai.url}")
    private String apiUrl;

    @Value("${api.ai.request-base}")
    private String baseRequest;

    @Value("${api.ai.request-format}")
    private String requestFormat;

    private static final String CONTENT_TYPE = "Content-Type";

    @Override
    public MessageCreateResponseDto createMessageForHubDeliver(
            MessageCreateRequestDto messageCreateRequestDto
    ) {
        String requestMessage = createRequestMessage(messageCreateRequestDto);
        String responseMessage = callApi(requestMessage);

        Ai aiMessage = Ai.builder()
                .hubDeliverId(messageCreateRequestDto.getDeliverId())
                .hubDeliverPlatformId(messageCreateRequestDto.getDeliverPlatformId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        // TODO : user 정보로 교체
        aiMessage.createdEntity(1L);
        aiRepository.save(aiMessage);

        return MessageCreateResponseDto.builder().message(responseMessage).build();
    }

    @Override
    public MessageCreateResponseDto createMessageForCompanyDeliver(
            MessageCreateRequestDto messageCreateRequestDto
    ) {
        String requestMessage = createRequestMessage(messageCreateRequestDto);
        String responseMessage = callApi(requestMessage);

        Ai aiMessage = Ai.builder()
                .consumerDeliverId(messageCreateRequestDto.getDeliverId())
                .consumerDeliverPlatformId(messageCreateRequestDto.getDeliverPlatformId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        // TODO : user 정보로 교체
        aiMessage.createdEntity(1L);
        aiRepository.save(aiMessage);

        return MessageCreateResponseDto.builder().message(responseMessage).build();
    }

    @Override
    public MessageFindResponseDto findMessage(UUID aiId) {

        Ai ai = aiRepository.findById(aiId).orElseThrow(
                () -> new AiException.MessageNotFoundException("Not Found Message"));

        return MessageFindResponseDto.form(ai);

    }

    private String createRequestMessage(MessageCreateRequestDto messageCreateRequestDto) {

        return requestFormat
                .replace("{orderId}", messageCreateRequestDto.getOrderId().toString())
                .replace("{ordererName}", messageCreateRequestDto.getOrdererName())
                .replace("{productInfo}", messageCreateRequestDto.getProductInfo())
                .replace("{start}", messageCreateRequestDto.getStartHub())
                .replace("{destination}", messageCreateRequestDto.getDestination())
                .replace("{deliverName}", messageCreateRequestDto.getDeliverName())
                .replace("{orderDescription}", messageCreateRequestDto.getOrderDescription())
                .replace("{departureDeadline}", messageCreateRequestDto.getDepartureDeadline());
    }

    private String callApi(String request) {

        String body = baseRequest.replace("{request}", request);
        String url = apiUrl + apiKey;

        JSONObject jsonObject = webClient.post()
                .uri(url)
                .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .map(JSONObject::new)
                .onErrorResume(throwable -> {
                    log.error("Fail : {}", url, throwable);
                    return Mono.empty();
                })
                .block();

        return jsonObject
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");
    }
}
