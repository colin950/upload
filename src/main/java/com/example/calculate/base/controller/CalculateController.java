package com.example.calculate.base.controller;

import com.example.calculate.base.dto.request.CalculateRequestDto;
import com.example.calculate.base.dto.request.CalculateTransferRequestDto;
import com.example.calculate.base.dto.response.CalculateDetailResponseDto;
import com.example.calculate.base.dto.response.CalculateResponseDto;
import com.example.calculate.base.model.CalculateResponseModel;
import com.example.calculate.utils.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.calculate.base.service.CalculateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "정산")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/calculate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalculateController {
    private final CalculateService calculateService;

    @GetMapping("/request/{id}")
    @Operation(summary = "정산 요청 확인 API", description = "정산 요청 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CalculateResponseDto.class))
            )
    })
    public ResponseEntity<?> getCalculateRequest(@PathVariable("id") Long id) {
        return ResponseEntity.ok(calculateService.getCalculateRequestData(id));
    }

    @GetMapping("/request/list")
    @Operation(summary = "정산 요청 전체 리스트 확인 API", description = "정산 요청 전체 리스트 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CalculateResponseDto.class))
            )
    })
    public ResponseEntity<?> getCalculateRequestList(HttpServletRequest request) {
        return ResponseEntity.ok(calculateService.getCalculateRequestList(UserUtils.getUserInfo(request)));
    }

    @GetMapping("/receiver/list")
    @Operation(summary = "본인의 정산 요청 받은 전체 리스트 확인 API", description = "본인의 정산 요청 받은 전체 리스트 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CalculateDetailResponseDto.class))
            )
    })
    public ResponseEntity<?> getCalculateRequestL(HttpServletRequest request) {
        return ResponseEntity.ok(calculateService.getCalculateReceiverList(UserUtils.getUserInfo(request)));
    }

    @PostMapping("/request")
    @Operation(summary = "정산 요청 API", description = "정산 요청")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CalculateResponseModel.class))
            )
    })
    public ResponseEntity<?> calculateRequest(
            @Valid @RequestBody CalculateRequestDto calculateRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(
                calculateService.getCalculateRequestData(
                        calculateService.calculateRequest(calculateRequestDto, UserUtils.getUserInfo(request))));
    }

    @PostMapping("/transfer")
    @Operation(summary = "정산 송금 API", description = "정산 송금")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CalculateResponseModel.class))
            )
    })
    public Long calculateTransfer(@Valid @RequestBody CalculateTransferRequestDto requestDto) {
        return calculateService.calculateTransfer(calculateService.validateCalculateTransfer(requestDto));
    }

}
