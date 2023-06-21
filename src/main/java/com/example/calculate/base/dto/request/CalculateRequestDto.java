package com.example.calculate.base.dto.request;

import com.example.calculate.base.code.CalculateType;
import com.example.calculate.base.code.validator.Enum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalculateRequestDto {

    private String title;

    private String message;

    private String amount;

    private LocalDateTime notifiedAt;

    @NotBlank
    @Enum(enumClass = CalculateType.class, codeCompare = true)
    private String calculateType;

    private List<CalculateDetailDto> data;
}
