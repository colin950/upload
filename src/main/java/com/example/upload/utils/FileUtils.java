package com.example.upload.utils;

import com.example.upload.base.model.ConvertVideoModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUtils {
    private final FFmpeg ffMpeg;

    private final FFprobe ffProbe;

    private final RedisUtil redisUtil;

    public FFmpegProbeResult getProbeResult(String filePath) {
        try {
            FFmpegProbeResult ffmpegProbeResult = ffProbe.probe(filePath);

            System.out.println("비트레이트 : "+ffmpegProbeResult.getStreams().get(0).bit_rate);
            System.out.println("채널 : "+ffmpegProbeResult.getStreams().get(0).channels);
            System.out.println("코덱 명 : "+ffmpegProbeResult.getStreams().get(0).codec_name);
            System.out.println("코덱 유형 : "+ffmpegProbeResult.getStreams().get(0).codec_type);
            System.out.println("해상도(너비) : "+ffmpegProbeResult.getStreams().get(0).width);
            System.out.println("해상도(높이) : "+ffmpegProbeResult.getStreams().get(0).height);
            System.out.println("포맷(확장자) : "+ffmpegProbeResult.getFormat().size);

            return ffmpegProbeResult;
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    public String convertVideoProp(ConvertVideoModel convertVideoModel) {
        try {
            Path destinationFile = convertVideoModel.getPath().resolve(
                            Paths.get("cvt_"+ convertVideoModel.getFileName()))
                    .normalize().toAbsolutePath();

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(convertVideoModel.getFfMpegProbeResult())
                    .overrideOutputFiles(true)
                    .addOutput(destinationFile.toString())
                    .setFormat(convertVideoModel.getFormat())
                    .setVideoCodec(convertVideoModel.getVideoCodec())
                    .setVideoResolution(convertVideoModel.getWidth(), convertVideoModel.getHeight())
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffMpeg, ffProbe);
            double nbFrames = (double) convertVideoModel.getFfMpegProbeResult()
                    .getStreams()
                    .get(0)
                    .nb_frames;
            executor.createJob(builder, progress -> {
                if (progress.frame > 0) {
                    String progressPercent = String.format("%.2f", ((progress.frame / nbFrames) * 100));
                    // 현재 진행률에 대해 redis에 같은 id에 대한 key값으로 업데이트 진행
                    // ttl설정에 대해서는 설정에 따라 따로 설정 가능
                    redisUtil.setConvertVideoProgressPercent(convertVideoModel.getFileInfoId(), progressPercent);
                    log.info("progress : " + progressPercent + "%");
                }
                if (progress.isEnd()) {
                    log.info(convertVideoModel.getFileName() +"convert success.!");
                }
            }).run();
            return destinationFile.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String exportThumbnail(File file) {
        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)
                .setInput(file.getAbsolutePath())
                .addExtraArgs("-ss", "00:00:01")
                .addOutput(getThumbnailName(file))
                .setFrames(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffMpeg, ffProbe);		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
        executor.createJob(builder).run();
        return getThumbnailName(file);
    }

    private String getThumbnailName(File file) {
        return file.getParent() + "/thmb_" + file.getName()+ ".png";
    }

}
