package com.yeonsung01.cryptopricetrackerapi.domain.coin.controller;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinCreateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinUpdateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinDeleteResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.service.CoinService;
import com.yeonsung01.cryptopricetrackerapi.global.response.ApiResponse;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Coin REST API 컨트롤러
 * - 컨트롤러는 요청/응답 처리만, 비즈니스 로직은 Service에 위임
 * - 모든 응답은 ApiResponse<T> 로 통일
 */
@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    /**
     * POST /api/v1/coins
     * 코인 등록
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CoinResponse>> createCoin(
            @Valid @RequestBody CoinCreateRequest request) {

        CoinResponse response = coinService.createCoin(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("코인이 등록되었습니다.", response));
    }

    /**
     * GET /api/v1/coins/{id}
     * 코인 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CoinResponse>> getCoin(@PathVariable Long id) {
        CoinResponse response = coinService.getCoin(id);
        return ResponseEntity.ok(ApiResponse.success("코인 조회 성공", response));
    }

    /**
     * GET /api/v1/coins?page=1&size=20
     * 코인 목록 조회 (페이지네이션)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CoinResponse>>> getCoins(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<CoinResponse> response = coinService.getCoins(page, size);
        return ResponseEntity.ok(ApiResponse.success("코인 목록 조회 성공", response));
    }

    /**
     * PATCH /api/v1/coins/{id}
     * 코인 수정 (부분 수정 가능)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CoinResponse>> updateCoin(
            @PathVariable Long id,
            @RequestBody CoinUpdateRequest request) {

        CoinResponse response = coinService.updateCoin(id, request);
        return ResponseEntity.ok(ApiResponse.success("코인이 수정되었습니다.", response));
    }

    /**
     * DELETE /api/v1/coins/{id}
     * 코인 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CoinDeleteResponse>> deleteCoin(@PathVariable Long id) {
        CoinDeleteResponse response = coinService.deleteCoin(id);
        return ResponseEntity.ok(ApiResponse.success("코인이 삭제되었습니다.", response));
    }
}
