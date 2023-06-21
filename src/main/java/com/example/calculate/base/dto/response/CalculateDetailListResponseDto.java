package com.example.calculate.base.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class CalculateDetailListResponseDto extends CalculateDetailResponseDto {

    private Long calculateInfoId;
    private BigDecimal totalAmount;

    private LocalDateTime notifySetupAt;

}
