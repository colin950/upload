package com.example.search.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.search.base.code.validator.SortCode;
import com.example.search.base.dto.response.PagingResponseDto;
import com.example.search.base.dto.response.SearchPlaceResponseDto;
import com.example.search.base.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "검색")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/v1/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {
    private final SearchService searchService;


    /**
     * response 순서
     * 1. 카카오, 네이버 결과 모두 존재
     * 2. 카카오 우선 배치
     * 3. 네이버 배치
     * @param keyword
     * @return
     */
    @GetMapping("/places/{keyword}")
    @Operation(summary = "장소 검색 API", description = "장소 검색")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SearchPlaceResponseDto.class))
            )
    })
    public ResponseEntity<?> searchPlaces(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(searchService.searchPlaces(keyword));
    }

    @GetMapping("/places/most")
    @Operation(summary = "많이 검색한 장소 조회 API", description = "장소 검색")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PagingResponseDto.class))
            )
    })
    public ResponseEntity<?> searchPlaces(
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "desc", name = "sort") String sort,
            @RequestParam(defaultValue = "cnt", name = "field") String field) {
        PageRequest pageRequest =
                PageRequest.of(
                        page - 1,
                        size,
                        Sort.by(
                                sort.equalsIgnoreCase(SortCode.desc.name())
                                        ? Sort.Direction.DESC
                                        : Sort.Direction.ASC,
                                field));
        return ResponseEntity.ok(new PagingResponseDto(searchService.findByKeyword(pageRequest)));
    }
}
