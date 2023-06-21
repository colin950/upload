package com.example.calculate.base.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
public class CalculateDetailResponseDto {
    private Long calculateDetailId;

    private Long senderUserId;

    private BigDecimal amount;

    private String status;

    private LocalDateTime notifySendAt;


}
