package com.example.upload.base.controller;


import com.example.upload.base.model.ConvertVideoModel;
import com.example.upload.base.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "업로드")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/file")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "파일 업로드 API", description = "파일 업로드")
    public Long postFileUpload(
            @RequestParam("file") MultipartFile file,
            @NotBlank @RequestParam("title") String title) {
        ConvertVideoModel convertVideoModel = fileService.upload(file, title);

        // 비동기 처리
        fileService.convertVideo(convertVideoModel);

        return convertVideoModel.getFileInfoId();
    }

    @GetMapping("/video/{id}/progress")
    public ResponseEntity<?> getVideoProgressInfo(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getVideoProgressInfo(id));
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<?> getVideoInfo(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getVideoInfo(id));
    }
}
