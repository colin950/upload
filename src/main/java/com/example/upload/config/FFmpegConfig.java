package com.example.upload.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FFmpegConfig {

    @Value("${ffmpeg.mpeg}")
    private String ffmpegLocation;

    @Value("${ffmpeg.probe}")
    private String ffProbeLocation;


    @Bean(name = "ffMpeg")
    public FFmpeg fFmpeg() throws IOException {
        ClassPathResource resource = new ClassPathResource(ffmpegLocation);
        return new FFmpeg(resource.getURL().getPath());
    }

    @Bean(name = "ffProbe")
    public FFprobe ffProbe() throws IOException {
        ClassPathResource resource = new ClassPathResource(ffProbeLocation);
        return new FFprobe(resource.getURL().getPath());
    }

}
