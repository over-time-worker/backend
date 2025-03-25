package com.owl_express.ai.application.service;

import static com.owl_express.ai.common.exceptions.ExceptionMessage.MESSAGE_NOT_FOUND_MESSAGE;

import com.owl_express.ai.common.dto.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.ai.common.dto.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.ai.common.dto.request.HubDeliverMessageCreateRequestDto.HubListDto;
import com.owl_express.ai.common.dto.response.MessageCreateResponseDto;
import com.owl_express.ai.common.dto.response.MessageFindResponseDto;
import com.owl_express.ai.application.exceptions.AiException.MessageNotFoundException;
import com.owl_express.ai.common.helper.PassportHelper;
import com.owl_express.ai.common.util.CommonUtil;
import com.owl_express.ai.common.util.PageUtil;
import com.owl_express.ai.domain.entity.Ai;
import com.owl_express.ai.domain.repository.AiRepository;
import com.owl_express.ai.presentation.AiService;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final PassportHelper passportHelper;

    @Value("${api.ai.key}")
    private String apiKey;

    @Value("${api.ai.url}")
    private String apiUrl;

    @Value("${api.ai.hub.request-base}")
    private String hubRequestBase;

    @Value("${api.ai.hub.request-format}")
    private String hubRequestFormat;

    @Value("${api.ai.company.request-base}")
    private String companyRequestBase;

    @Value("${api.ai.company.request-format}")
    private String companyRequestFormat;

    private static final String CONTENT_TYPE = "Content-Type";

    @Override
    public MessageCreateResponseDto createMessageForHubDeliver(
            HubDeliverMessageCreateRequestDto hubDeliverMessageCreateRequestDto,
            String passport
    ) {
        String requestMessage = createHubRequestMessage(hubDeliverMessageCreateRequestDto);
        String responseMessage = callApi(hubRequestBase.replace("{request}", requestMessage));

        Ai aiMessage = Ai.builder()
                .hubDeliverId(hubDeliverMessageCreateRequestDto.getDeliverId())
                .hubDeliverChannelId(hubDeliverMessageCreateRequestDto.getDeliverChannelId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        aiMessage.createdEntity(passportHelper.getPassportDto(passport).getUserId());
        Ai savedAiMessage = aiRepository.save(aiMessage);

        return MessageCreateResponseDto.toDto(savedAiMessage);
    }

    @Override
    public MessageCreateResponseDto createMessageForCompanyDeliver(
            CompanyDeliverMessageCreateRequestDto companyDeliverMessageCreateRequestDto,
            String passport
    ) {
        String requestMessage = createCompanyRequestMessage(companyDeliverMessageCreateRequestDto);
        String responseMessage = callApi(companyRequestBase.replace("{request}", requestMessage));

        Ai aiMessage = Ai.builder()
                .consumerDeliverId(companyDeliverMessageCreateRequestDto
                        .getDeliverId())
                .consumerDeliverChannelId(companyDeliverMessageCreateRequestDto.getDeliverChannelId())
                .request(requestMessage)
                .response(responseMessage)
                .build();

        aiMessage.createdEntity(passportHelper.getPassportDto(passport).getUserId());
        Ai savedAiMessage = aiRepository.save(aiMessage);

        return MessageCreateResponseDto.toDto(savedAiMessage);
    }

    @Override
    public MessageFindResponseDto find(UUID aiId) {

        Ai ai = aiRepository.findById(aiId).orElseThrow(
                () -> new MessageNotFoundException(MESSAGE_NOT_FOUND_MESSAGE));

        return MessageFindResponseDto.toDto(ai);

    }

    @Override
    public PagedModel<MessageFindResponseDto> search(int page, int size, String sort, String orderBy, UUID keyword) {
        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<MessageFindResponseDto> paged = aiRepository.searchMessages(pageable, keyword);
        return new PagedModel<>(paged);
    }

    @Override
    public void delete(UUID aiId, String passport) {
        Ai ai = aiRepository.findById(aiId).orElseThrow(
                () -> new MessageNotFoundException(MESSAGE_NOT_FOUND_MESSAGE));

        ai.deleteEntity(passportHelper.getPassportDto(passport).getUserId());
        aiRepository.save(ai);
    }

    private String createHubRequestMessage(HubDeliverMessageCreateRequestDto messageCreateRequestDto) {

        String hubNameList = "[ " +
                messageCreateRequestDto.getTotalHubList().stream()
                        .map(HubListDto::getHubName)
                        .collect(Collectors.joining(" "))
                + " ]";

        String hubDurationTimeList = "[ " +
                messageCreateRequestDto.getTotalHubList().stream()
                        .map(HubListDto::getEstimateDurationTime)
                        .map(duration -> Optional.ofNullable(duration)
                                .map(d -> d.toMillis() + "ms ")
                                .orElse("N/A "))
                        .limit(Math.max(0, messageCreateRequestDto.getTotalHubList().size() - 1))
                        .collect(Collectors.joining(" "))
                + " ]";

        return hubRequestFormat
                .replace("{orderId}", messageCreateRequestDto.getOrderId().toString())
                .replace("{ordererName}", messageCreateRequestDto.getOrdererName())
                .replace("{productInfo}", messageCreateRequestDto.getProductInfo())
                .replace("{startHub}", messageCreateRequestDto.getStartHubName())
                .replace("{endHub}", messageCreateRequestDto.getEndHubName())
                .replace("{currentHub}", messageCreateRequestDto.getCurrentHubName())
                .replace("{nextHub}", messageCreateRequestDto.getNextHubName())
                .replace("{hubList}", hubNameList)
                .replace("{durationTime}", hubDurationTimeList)
                .replace("{deliverName}", messageCreateRequestDto.getDeliverName())
                .replace("{orderDescription}", messageCreateRequestDto.getOrderDescription())
                .replace("{requestArrivalTime}", CommonUtil.LocalDateTimetoString(messageCreateRequestDto.getRequestArrivalTime()))
                .replace("{totalDistance}", messageCreateRequestDto.getTotalEstimateDistance() + "m")
                .replace("{totalDuration}", messageCreateRequestDto.getTotalEstimateDurationTime().toMillis() + "ms");


    }

    private String createCompanyRequestMessage(CompanyDeliverMessageCreateRequestDto messageCreateRequestDto) {

        return companyRequestFormat
                .replace("{orderId}", messageCreateRequestDto.getOrderId().toString())
                .replace("{ordererName}", messageCreateRequestDto.getOrdererName())
                .replace("{productInfo}", messageCreateRequestDto.getProductInfo())
                .replace("{startHub}", messageCreateRequestDto.getStartHubName())
                .replace("{shippingAddress}", messageCreateRequestDto.getShippingAddress())
                .replace("{deliverName}", messageCreateRequestDto.getDeliverName())
                .replace("{orderDescription}", messageCreateRequestDto.getOrderDescription())
                .replace("{requestArrivalTime}", CommonUtil.LocalDateTimetoString(messageCreateRequestDto.getRequestArrivalTime()));
    }

    private String callApi(String body) {

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
