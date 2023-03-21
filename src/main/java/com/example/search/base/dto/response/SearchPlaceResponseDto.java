package com.example.search.base.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.example.search.base.dto.BaseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchPlaceResponseDto extends BaseDto {
    private String placeName;
}
