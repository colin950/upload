package com.example.calculate.service;

import com.example.calculate.AcceptanceTest;
import com.example.calculate.base.dto.request.CalculateRequestDto;
import com.example.calculate.base.dto.response.CalculateResponseDto;
import com.example.calculate.exception.UnprocessableException;
import com.example.calculate.security.UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("정산 요청")
public class CalculateRequestTest extends AcceptanceTest {

    private CalculateRequestDto request = setUpRequestDto();

    private UserInfo userInfo = setUpUserInfo();
    @DisplayName("정산 요청 성공")
    @Test
    void successSettleRequest() {
        Long id = calculateService.calculateRequest(request, userInfo);
        assertNotNull(id);
    }

    @DisplayName("정산 요청 성공 데이터 확인")
    @Test
    void successSettleRequestData() {
        CalculateResponseDto result = calculateService.getCalculateRequestData(
                calculateService.calculateRequest(request, userInfo));

        // 총 합계 확인
        assertEquals(
                new BigDecimal(request.getAmount()),
                result.getTotalAmount().setScale(0, RoundingMode.DOWN));

        List<Long> requestUserIds = request.getData().stream()
                .map(it -> it.getUserId())
                .collect(Collectors.toList());

        List<Long> resultUserIds = result.getDetail().stream()
                .map(it -> it.getSenderUserId())
                .collect(Collectors.toList());

        // 요청한 유저들에가 정상적으로 데이터 반영이 되었는지
        assertThat(requestUserIds).containsExactlyElementsOf(resultUserIds);
    }

    @DisplayName("금액으로 인핸 정산 요청 실패")
    @Test
    void failSettleRequestForAmount() {
        // 총 금액
        request.setAmount("1000");

        request.getData().stream().findFirst().get().setAmount("1000");

        assertThrows(UnprocessableException.class, () -> {
            // 총 금액 : 1000, 정산 금액 : 1000, 500 = 1500
            calculateService.calculateRequest(request, userInfo);
        });
    }



}
