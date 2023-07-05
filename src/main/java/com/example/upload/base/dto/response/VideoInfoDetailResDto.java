package com.example.upload.base.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VideoInfoDetailResDto {
    private Long fileSize;

    private int width;

    private int height;

    private String videoUrl;

    public VideoInfoDetailResDto(Long fileSize, int width, int height, String videoUrl) {
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.videoUrl = videoUrl;
    }

}
