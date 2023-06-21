package com.example.calculate.service;


import com.example.calculate.AcceptanceTest;
import com.example.calculate.base.code.CalculateStatusCd;
import com.example.calculate.base.dto.request.CalculateRequestDto;
import com.example.calculate.base.dto.response.CalculateResponseDto;
import com.example.calculate.entity.CalculateDetailEntity;
import com.example.calculate.repository.CalculateDetailRepository;
import com.example.calculate.security.UserInfo;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("정산 송금")
public class CalculateTransferTest extends AcceptanceTest {
    private CalculateResponseDto result;

    @Autowired
    private CalculateDetailRepository calculateDetailRepository;

    private CalculateRequestDto request = setUpRequestDto();

    private UserInfo userInfo = setUpUserInfo();

    @BeforeEach
    void setUp() {
        result = calculateService.getCalculateRequestData(
                calculateService.calculateRequest(request, userInfo));
    }

    @DisplayName("정산 송금 성공")
    @Test
    void successTransfer() {
        // 정산 송금 한건 중 첫번째에 해당하는 사람에 대해 처리
        Long id = calculateService.calculateTransfer(
                result.getDetail().stream()
                        .findFirst().get().getCalculateDetailId());
        String currentStatus = result.getDetail().stream()
                .findFirst().get().getStatus();

        // 송금 전 상태 값
        assertThat(currentStatus).isEqualTo(CalculateStatusCd.ready.name());

        CalculateResponseDto result = calculateService.getCalculateRequestData(id);
        String resultStatus = result.getDetail().stream()
                .findFirst().get().getStatus();

        // 송금 이후 상태 값
        assertThat(resultStatus).isEqualTo(CalculateStatusCd.done.name());
    }

    @DisplayName("미송금자에 대한 알림 기능")
    @Test
    void successNotify() {
        calculateService.unsettledNotify();
        List<CalculateDetailEntity> result =calculateDetailRepository.findAll();

        List<LocalDateTime> sentAtList = result.stream()
                .map(it -> it.getNotifySendAt())
                .collect(Collectors.toList());

        assertThat(sentAtList).doesNotContainNull();
    }
}
