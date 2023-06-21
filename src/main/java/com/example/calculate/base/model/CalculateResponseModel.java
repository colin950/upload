package com.example.calculate.base.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalculateResponseModel {
    private Long id;

    private Long requestUserId;

    private BigDecimal totalAmount;

    private LocalDateTime notifySetupAt;

    private String mainStatus;

    private Long calculateDetailId;

    private Long senderUserId;

    private BigDecimal amount;

    private String status;

    public CalculateResponseModel(Long id,
                                  Long requestUserId,
                                  BigDecimal totalAmount,
                                  LocalDateTime notifySetupAt,
                                  String mainStatus,
                                  Long calculateDetailId,
                                  Long senderUserId,
                                  BigDecimal amount,
                                  String status) {
        this.id = id;
        this.requestUserId = requestUserId;
        this.totalAmount = totalAmount;
        this.notifySetupAt = notifySetupAt;
        this.mainStatus = mainStatus;
        this.calculateDetailId = calculateDetailId;
        this.senderUserId = senderUserId;
        this.amount = amount;
        this.status = status;
    }
}
