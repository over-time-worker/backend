package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.application.HubDistanceService;
import com.owlexpress.hub.application.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.hub.application.dto.response.HubProductStockResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
import com.owlexpress.hub.common.helper.HubHelper;
import com.owlexpress.hub.common.exception.HubException.HubNotFoundException;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException;
import com.owlexpress.hub.common.helper.PassportHelper;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCheckRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;
    private final HubIntervalInfoRepository hubIntervalInfoRepository;
    private static final List<Integer> ALLOWED_SIZES = List.of(10, 30, 50);
    private static final Integer DEFAULT_SIZE = 10;
    private final HubDistanceService hubDistanceService;
    private final PassportHelper passportHelper;

    @Override
    @Transactional
    public void create(HubCreateRequestDto requestDto,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Hub hub = null;
        //중앙 허브라면 바로 저장
        if (requestDto.getParentId() == null) {
            hub = requestDto.toEntity();
        } else {
            //스포크 허브라면 받은 중앙 허브값 확인
            hubRepository.findByHubId(requestDto.getParentId())
                    .ifPresent(findparentHub -> {
                        //스포크허브와 중앙 허브 연결
                        Hub spokeHub = requestDto.toEntity(findparentHub);
                        spokeHub.createdEntity(passportDto.getUserId());
                        hubRepository.save(spokeHub);
                    });
        }
        Objects.requireNonNull(hub)
                .createdEntity(passportDto.getUserId());
        hub.createdEntity(passportDto.getUserId());
        hubRepository.save(hub);
        hubIntervalInfoRepository.deleteContainsHub(hub);
    }



    @Transactional
    @Override
    public void update(
            HubUpdateRequestDto requestDto,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Hub hub = hubRepository.findByHubId(requestDto.getHubId())
                .orElseThrow(HubNotFoundException::new);

        hub.modifiedEntity(passportDto.getUserId());
        hub.update(requestDto);
        //위도 경도에 변화가 있는 경우 기존 기록 지우고 최신화
        if (hub.getLocation()
                .getX() != requestDto.getLatitude() || hub.getLocation()
                .getY() != requestDto.getLongitude()) {
            hubIntervalInfoRepository.deleteContainsHub(hub);
            hubDistanceService.calculateHubDistances(hub);
        }
        hub.modifiedEntity(passportDto.getUserId());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<HubSearchResponseDto> searchHub(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
        if (!ALLOWED_SIZES.contains(size)) {
            size = DEFAULT_SIZE;
        }
        Sort sortAndOrderBy = Sort.by(direction, orderBy); // direction 활용 추가
        PageRequest pageRequest = PageRequest.of(page, size, sortAndOrderBy);

        return hubRepository.searchHub(pageRequest, q, sort, orderBy);
    }

    @Override
    @Transactional(readOnly = true)
    public HubFindResponseDto find(UUID hubId) {
        return HubFindResponseDto.fromEntity(HubHelper.findByHubId(hubId, hubRepository));

    }


    @Override
    @Transactional
    public void delete(UUID hubId,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Hub hub = HubHelper.findByHubId(hubId, hubRepository);

        hub.deleteEntity(passportDto.getUserId());

        // 1. 중앙 허브인 경우
        if (hub.getParentHub() == null) {
            // 중앙 허브의 스포크 허브들 조회
            List<Hub> spokeHubs = hubRepository.findAllByParentHub(hub);

            if (!spokeHubs.isEmpty()) {
                // 중앙 허브의 물품 목록 가져오기
                List<HubProduct> hubProducts = hub.getHubProduct();

                // 물품을 스포크 허브에 랜덤하게 배분
                Random random = new Random();
                for (HubProduct product : hubProducts) {
                    Hub randomSpokeHub = spokeHubs.get(random.nextInt(spokeHubs.size()));
                    product.setHub(randomSpokeHub); // 물품 소유자를 변경
                }
            }

            // 중앙 허브 삭제
            hub.deleteEntity(1L);
            return;
        }

        // 2. 스포크 허브인 경우 (삭제 시 중앙 허브에 물품 추가)
        Hub centralHub = hub.getParentHub();
        hub.getHubProduct()
           .forEach(product -> product.setHub(centralHub));

        // 스포크 허브 삭제
        hub.deleteEntity(1L);

        //허브가 들어간 경로 모두 삭제
        hubIntervalInfoRepository.deleteContainsHub(hub);
    }

}
