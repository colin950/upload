package com.example.calculate.base.dto.response;

import com.example.calculate.base.model.CalculateResponseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalculateResponseDto {
    private Long id;

    private Long requestUserId;

    private BigDecimal totalAmount;

    private LocalDateTime notifySetupAt;

    private String mainStatus;

    private List<CalculateDetailResponseDto> detail = new ArrayList<>();

    public CalculateResponseDto(List<CalculateResponseModel> list) {
        this.id = list.stream().findFirst().get().getId();
        this.requestUserId = list.stream().findFirst().get().getRequestUserId();
        this.totalAmount = list.stream().findFirst().get().getTotalAmount();
        this.notifySetupAt = list.stream().findFirst().get().getNotifySetupAt();
        this.mainStatus = list.stream().findFirst().get().getMainStatus();
        list.forEach(d -> {
            detail.add(new CalculateDetailResponseDto()
                    .setCalculateDetailId(d.getCalculateDetailId())
                    .setSenderUserId(d.getSenderUserId())
                    .setAmount(d.getAmount())
                    .setStatus(d.getStatus())
            );
        });
    }

    public CalculateResponseDto(List<CalculateResponseModel> list, String type) {
        this.requestUserId = list.stream().findFirst().get().getRequestUserId();
        list.forEach(d -> {
            detail.add(new CalculateDetailListResponseDto()
                    .setCalculateInfoId(d.getId())
                    .setTotalAmount(d.getTotalAmount())
                    .setNotifySetupAt(d.getNotifySetupAt())
                    .setCalculateDetailId(d.getCalculateDetailId())
                    .setSenderUserId(d.getSenderUserId())
                    .setAmount(d.getAmount())
                    .setStatus(d.getStatus()));
        });
    }
}
