package com.example.search.base.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagingResponseDto {
    private Object content;

    @JsonProperty("total_elements")
    private long totalElements;

    public PagingResponseDto(Page page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
    }
}
