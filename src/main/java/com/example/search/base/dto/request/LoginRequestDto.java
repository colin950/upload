package com.example.search.base.dto.request;

import com.example.search.base.code.validator.Enum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import com.example.search.base.code.TelecomType;
import com.example.search.base.code.validator.PatternCustom;
import com.example.search.base.code.validator.PatternTypeCode;
import com.example.search.base.dto.BaseDto;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginRequestDto extends BaseDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;


    @PatternCustom(type = PatternTypeCode.email)
    private String phone;
    @NotBlank
    @Enum(enumClass = TelecomType.class, codeCompare = true)
    private String telecomeCode;
}
