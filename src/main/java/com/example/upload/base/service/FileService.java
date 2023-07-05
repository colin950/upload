package com.example.upload.base.service;

import com.example.upload.base.code.FileInfoStatusCd;
import com.example.upload.base.dto.response.GetVideoInfoResDto;
import com.example.upload.base.dto.response.GetVideoProgressResDto;
import com.example.upload.base.model.ConvertVideoModel;
import com.example.upload.entity.FileConvertInfoEntity;
import com.example.upload.entity.FileInfoEntity;
import com.example.upload.exception.UnprocessableException;
import com.example.upload.exception.code.ErrorCode;
import com.example.upload.repository.FileConvertInfoRepository;
import com.example.upload.repository.FileInfoRepository;
import com.example.upload.repository.querydsl.FileInfoQueryRepository;
import com.example.upload.utils.FileUtils;
import com.example.upload.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final ApplicationContext context;

    private final FileUtils fileUtils;

    private final FileInfoRepository fileInfoRepository;

    private final FileConvertInfoRepository fileConvertInfoRepository;

    private final FileInfoQueryRepository fileInfoQueryRepository;

    private final RedisUtil redisUtil;

    private final Path rootLocation = Paths.get("upload-file");

    @Transactional
    public ConvertVideoModel upload(MultipartFile file, String title) {
        try {
            if (file.isEmpty()) {
                throw new UnprocessableException(ErrorCode.DEMO_COMMON_INVALID_PARAM);
            }
            Path destinationFile = rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();

            // file 명의 경우 중복 될 수도 있으니 이미 존재할 경우 갈아엎기
            // 그 밖에 중복된 이미지가 들어가야 된다면 UUID()등을 이용
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            FFmpegProbeResult result = fileUtils.getProbeResult(destinationFile.toString());
            ConvertVideoModel convertVideoModel = new ConvertVideoModel()
                    .setFfMpegProbeResult(result)
                    .setWidth(360)
                    .setHeight(result.getStreams().get(0).height)
                    .setPath(rootLocation)
                    .setFileName(file.getOriginalFilename());

            // 썸네일 추출 (png)
            String thumbnailUrl = fileUtils.exportThumbnail(new File(destinationFile.toString()));

            FileInfoEntity fileInfo = new FileInfoEntity()
                    .setTitle(title)
                    .setFileSize(convertVideoModel.getFfMpegProbeResult().getFormat().size)
                    .setWidth(convertVideoModel.getWidth())
                    .setHeight(convertVideoModel.getHeight())
                    .setVideoUrl(destinationFile.toString())
                    .setStatus(FileInfoStatusCd.upload.name())
                    .setThumbUrl(thumbnailUrl);
            fileInfoRepository.save(fileInfo);

            convertVideoModel.setFileInfoId(fileInfo.getId());

            return convertVideoModel;
        } catch (IOException e) {
            throw new UnprocessableException(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR);
        }
    }
    public void convertVideo(ConvertVideoModel convertVideoModel) {
        CompletableFuture.runAsync(() -> {
            String convertFilePath = fileUtils.convertVideoProp(convertVideoModel);

            getProxy().updateCompletedFileInfo(
                    convertVideoModel.getFileInfoId(),
                    convertFilePath);
        });
    }

    public GetVideoInfoResDto getVideoInfo(Long id) {
        GetVideoInfoResDto result = fileInfoQueryRepository.findByIdWithFileConvertInfo(id);
        if (result == null) {
            throw new UnprocessableException(ErrorCode.DEMO_FILE_NOT_FOUND);
        }

        return result;
    }

    public GetVideoProgressResDto getVideoProgressInfo(Long id) {
        String percent = redisUtil.getConvertVideoProgressPercent(id);
        return new GetVideoProgressResDto()
                .setId(id)
                .setProgress(percent + "%");
    }

    @Transactional
    public void updateCompletedFileInfo(Long id, String convertFilePath) {
        FileInfoEntity fileInfo = fileInfoRepository.findById(id)
                .orElseThrow(
                        () -> new UnprocessableException(ErrorCode.DEMO_COMMON_UNKNOWN_ERROR));
        fileInfo.setCompletedAt(LocalDateTime.now());
        fileInfo.setStatus(FileInfoStatusCd.completed.name());

        FFmpegProbeResult result = fileUtils.getProbeResult(convertFilePath);

        FileConvertInfoEntity fileConvertInfo = new FileConvertInfoEntity();
        fileConvertInfo.setFileInfoId(fileInfo.getId());
        fileConvertInfo.setFileSize(result.getFormat().size);
        fileConvertInfo.setWidth(result.getStreams().get(0).width);
        fileConvertInfo.setHeight(result.getStreams().get(0).height);
        fileConvertInfo.setVideoUrl(convertFilePath);


        fileConvertInfoRepository.save(fileConvertInfo);
        fileInfoRepository.save(fileInfo);
    }

    // proxy를 가져와서 사용
    // transaction 중간에 사용시에 사용
    private FileService getProxy() { return context.getBean(FileService.class); }
}
