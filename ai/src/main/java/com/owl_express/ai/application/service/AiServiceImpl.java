package com.owl_express.ai.application.service;

import static com.owl_express.ai.common.exceptions.ExceptionMessage.MESSAGE_NOT_FOUND_MESSAGE;

import com.owl_express.ai.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import com.owl_express.ai.application.exceptions.AiException;
import com.owl_express.ai.common.util.PageUtil;
import com.owl_express.ai.domain.entity.Ai;
import com.owl_express.ai.domain.repository.AiRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

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
                .hubDeliverChannelId(messageCreateRequestDto.getDeliverChannelId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        // TODO : user 정보로 교체
        aiMessage.createdEntity(1L);
        Ai savedAiMessage = aiRepository.save(aiMessage);

        return MessageCreateResponseDto.toDto(savedAiMessage);
    }

    @Override
    public MessageCreateResponseDto createMessageForCompanyDeliver(
            MessageCreateRequestDto messageCreateRequestDto
    ) {
        String requestMessage = createRequestMessage(messageCreateRequestDto);
        String responseMessage = callApi(requestMessage);

        Ai aiMessage = Ai.builder()
                .consumerDeliverId(messageCreateRequestDto.getDeliverId())
                .consumerDeliverChannelId(messageCreateRequestDto.getDeliverChannelId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        // TODO : user 정보로 교체
        aiMessage.createdEntity(1L);
        Ai savedAiMessage = aiRepository.save(aiMessage);

        return MessageCreateResponseDto.toDto(savedAiMessage);
    }

    @Override
    public MessageFindResponseDto find(UUID aiId) {

        Ai ai = aiRepository.findById(aiId).orElseThrow(
                () -> new AiException.MessageNotFoundException(MESSAGE_NOT_FOUND_MESSAGE));

        return MessageFindResponseDto.toDto(ai);

    }

    @Override
    public PagedModel<MessageFindResponseDto> search(int page, int size, String sort, String orderBy, UUID keyword) {
        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<MessageFindResponseDto> paged = aiRepository.searchMessages(pageable, keyword);
        return new PagedModel<>(paged);
    }

    private String createRequestMessage(MessageCreateRequestDto messageCreateRequestDto) {

        return requestFormat
                .replace("{orderId}", messageCreateRequestDto.getOrderId())
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
