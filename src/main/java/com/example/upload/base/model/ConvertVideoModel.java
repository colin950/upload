package com.example.upload.base.model;

import lombok.Data;
import lombok.experimental.Accessors;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.nio.file.Path;

@Data
@Accessors(chain = true)
public class ConvertVideoModel {

    private Long fileInfoId;

    private FFmpegProbeResult ffMpegProbeResult;
    private String format = "mp4";

    private String videoCodec = "h264";

    private int channel = 0;

    private int width;

    private int height;

    private Path path;

    private String fileName;

}
