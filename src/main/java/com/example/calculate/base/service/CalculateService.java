package com.example.calculate.base.service;

import com.example.calculate.base.code.CalculateStatusCd;
import com.example.calculate.base.code.CurrencyCd;
import com.example.calculate.base.code.HistType;
import com.example.calculate.base.dto.request.CalculateRequestDto;
import com.example.calculate.base.dto.request.CalculateTransferRequestDto;
import com.example.calculate.base.dto.response.CalculateDetailListResponseDto;
import com.example.calculate.base.dto.response.CalculateDetailResponseDto;
import com.example.calculate.base.dto.response.CalculateResponseDto;
import com.example.calculate.base.model.CalculateResponseModel;
import com.example.calculate.entity.CalculateDetailEntity;
import com.example.calculate.entity.CalculateInfoEntity;
import com.example.calculate.entity.TransferHistEntity;
import com.example.calculate.exception.UnprocessableException;
import com.example.calculate.exception.code.ErrorCode;
import com.example.calculate.push.service.PushClient;
import com.example.calculate.repository.CalculateDetailRepository;
import com.example.calculate.repository.CalculateInfoRepository;
import com.example.calculate.repository.TransferHistRepository;
import com.example.calculate.repository.querydsl.CalculateInfoQueryRepository;
import com.example.calculate.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculateService {

    private final CalculateInfoRepository calculateInfoRepository;

    private final CalculateDetailRepository calculateDetailRepository;

    private final CalculateInfoQueryRepository calculateInfoQueryRepository;

    private final TransferHistRepository transferHistRepository;

    private final PushClient pushClient;

    private final ApplicationContext context;

    /**
     * 요청 건별 리스트
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public CalculateResponseDto getCalculateRequestData(Long id) {
        List<CalculateResponseModel> result = calculateInfoQueryRepository.findByDynamicQuery(id, null);
        if (result.size() == 0 ) {
            return null;
        }

        return new CalculateResponseDto(result);
    }

    /**
     * 요청자 기준 전체 리스트
     * @return
     */
    @Transactional(readOnly = true)
    public CalculateResponseDto getCalculateRequestList(UserInfo userInfo) {
        List<CalculateResponseModel> result =
                calculateInfoQueryRepository
                        .findByDynamicQuery(null, userInfo.getUserId());
        if (result.size() == 0 ) {
            return null;
        }
        return new CalculateResponseDto(result, "list");
    }

    /**
     * 요청 받은 기준 전체 리스트
     * @return
     */
    @Transactional(readOnly = true)
    public List<CalculateDetailResponseDto> getCalculateReceiverList(UserInfo userInfo) {
        return calculateDetailRepository
                .findAllBySenderUserId(userInfo.getUserId()).stream()
                .map(m -> new CalculateDetailListResponseDto()
                        .setCalculateInfoId(m.getCalculateInfoId())
                        .setAmount(m.getAmount())
                        .setStatus(m.getStatus()))
                .collect(Collectors.toList());
    }

    public Long calculateRequest(CalculateRequestDto calculateRequestDto, UserInfo userInfo) {
        // 요청 파라미터 확인
        validateCalculateDto(calculateRequestDto);

        // transaction 적용을 위해해
        Long calculateInfoId = getProxy().saveCalculateRequest(calculateRequestDto, userInfo);
        return calculateInfoId;
    }

    private void validateCalculateDto(CalculateRequestDto calculateRequestDto) {
        // 정산 요청 대상이 없을 경우
        if (calculateRequestDto.getData().size() == 0) {
            throw new UnprocessableException(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR);
        }

        BigDecimal requestTotalAmount = calculateRequestDto.getData().stream()
                .map(c -> new BigDecimal(c.getAmount()))
                .collect(Collectors.toList())
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int compareResult = requestTotalAmount.compareTo(new BigDecimal(calculateRequestDto.getAmount()));
        if (compareResult > 0 || compareResult < 0) {
            throw new UnprocessableException(ErrorCode.DEMO_CALCULATE_DISAGREE);
        }
        // ... validation 추가
    }

    @Transactional
    public Long saveCalculateRequest(CalculateRequestDto calculateRequestDto, UserInfo userInfo) {
        // 요청 원장 저장
        CalculateInfoEntity calculateInfoEntity = new CalculateInfoEntity()
                .setCurrency(CurrencyCd.krw.name())
                .setTotalAmount(new BigDecimal(calculateRequestDto.getAmount()))
                .setStatus(CalculateStatusCd.ready.name())
                .setNotifySetupAt(calculateRequestDto.getNotifiedAt())
                .setXRoomId(userInfo.getRoomId())
                .setUserId(userInfo.getUserId());

        calculateInfoRepository.save(calculateInfoEntity);


        // 요청 원장에 대한 세부 내역 저장 (정산 해야하는 다수의 사용자)
        // 하나씩 저장하는 것보다 saveAll사용하는 것이 유리
        List<CalculateDetailEntity> calculateDetailEntityList = new ArrayList<>();
        calculateRequestDto.getData()
                .stream()
                .forEach(entity -> {
                    CalculateDetailEntity calculateDetail = new CalculateDetailEntity()
                            .setCalculateInfoId(calculateInfoEntity.getId())
                            .setSenderUserId(entity.getUserId())
                            .setAmount(new BigDecimal(entity.getAmount()))
                            .setStatus(CalculateStatusCd.ready.name());

                    calculateDetailEntityList.add(calculateDetail);
                });
        calculateDetailRepository.saveAll(calculateDetailEntityList);
        return calculateInfoEntity.getId();
    }

    @Transactional(readOnly = true)
    public Long validateCalculateTransfer(CalculateTransferRequestDto requestDto) {
        CalculateDetailEntity detail =
                calculateDetailRepository
                        .findById(requestDto.getCalculateDetailId())
                        .orElseThrow(() ->
                                new UnprocessableException(ErrorCode.DEMO_CALCULATE_NOT_FOUND));

        // 이미 처리 완료
        if (detail.getStatus().equals(CalculateStatusCd.done.name())) {
            throw new UnprocessableException(ErrorCode.DEMO_CALCULATE_ALREADY_DONE);
        }
        return detail.getId();
        // .... 등등의 validation 진행
    }

    /**
     * 전재 : 자산이 충분하다, 친구 여부 상관 x, 금액 이체 및 알림 발송 실제 구현 x
     * @param id
     */
    @Transactional
    public Long calculateTransfer(Long id) {
        // 송금자 정보 추출
        CalculateDetailEntity detail = calculateDetailRepository
                .findByIdAndStatus(
                        id, CalculateStatusCd.ready.name())
                .orElseThrow(() ->
                        new UnprocessableException(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR));

        // 요청자 정보 추출
        CalculateInfoEntity info = calculateInfoRepository
                .findById(detail.getCalculateInfoId())
                .orElseThrow(() ->
                        new UnprocessableException(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR));

        // 완료 상태 업데이트
        detail.setStatus(CalculateStatusCd.done.name());

        // 전송 이력 업데이트
        // hist_id, hist_type이 unique로 묶여 있어 동시성 처리 방지(중복 송금 방지)
        // 실제 동시에 요청이 들어와 insert 치려할때
        // unique key값으로 인해 해당 transaction 전부 rollback처리
        TransferHistEntity transferHist = new TransferHistEntity()
                .setHistId(detail.getId())
                .setHistType(HistType.calculate.name())
                .setAmount(detail.getAmount())
                .setSenderUserId(detail.getSenderUserId())
                .setReceiverUserId(info.getUserId());

        // 송금자 자산 업데이트(자산 마이너스)
        updateWallet(detail.getSenderUserId(), detail.getAmount());
        // 요청자 자산 업데이트(자산 플러스)
        updateWallet(detail.getSenderUserId(), detail.getAmount());

        confirmCalculateInfo(detail.getCalculateInfoId());

        calculateDetailRepository.save(detail);
        transferHistRepository.save(transferHist);
        return info.getId();
    }

    @Transactional
    public void updateWallet(Long userId, BigDecimal amount) {
        //.... 자산 이동 DB업데이트 및 자산 처리 서비스가 따로 있을 경우 호출
    }

    // 요청에 대한 송금이 전부 이루어 졌다면 원장 info데이터에도 완료 처리
    @Transactional
    public void confirmCalculateInfo(Long calculateInfoId) {
        List<CalculateDetailEntity> detail = calculateDetailRepository
                .findAllByCalculateInfoIdAndStatus(
                        calculateInfoId, CalculateStatusCd.ready.name());
        if (detail.size() < 1) {
            CalculateInfoEntity info = calculateInfoRepository.getById(calculateInfoId);
            info.setStatus(CalculateStatusCd.done.name());
            calculateInfoRepository.save(info);
        }
    }

    @Transactional
    public void unsettledNotify() {
        List<CalculateResponseModel> list = calculateInfoQueryRepository.findAllAndNotify();
        List<CalculateDetailEntity> detailList = new ArrayList<>();
        list.forEach(l -> {
            // 유저마다 푸시 진행
            pushClient.push();
            CalculateDetailEntity detail =
                    calculateDetailRepository.getById(l.getCalculateDetailId())
                            .setNotifySendAt(LocalDateTime.now());
            detailList.add(detail);

        });
        calculateDetailRepository.saveAll(detailList);
    }


    // proxy를 가져와서 사용
    // transaction 중간에 사용시에 사용
    private CalculateService getProxy() { return context.getBean(CalculateService.class); }
}
