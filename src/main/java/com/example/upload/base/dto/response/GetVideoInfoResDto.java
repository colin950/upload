package com.example.upload.base.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetVideoInfoResDto {
    private Long id;

    private String title;

    private VideoInfoDetailResDto original;

    private VideoInfoDetailResDto resized;
    private LocalDateTime createdAt;

    public GetVideoInfoResDto(Long id, String title, VideoInfoDetailResDto original, VideoInfoDetailResDto resized, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.original = original;
        this.resized = resized;
        this.createdAt = createdAt;
    }
}
