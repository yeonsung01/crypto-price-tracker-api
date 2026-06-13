package com.yeonsung01.cryptopricetrackerapi.domain.price.controller;

import com.yeonsung01.cryptopricetrackerapi.domain.price.dto.response.PriceResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.price.service.PriceService;
import com.yeonsung01.cryptopricetrackerapi.global.response.ApiResponse;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coins/{coinId}/price")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * GET /api/v1/coins/{coinId}/price
     * 특정 코인 최신 가격
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PriceResponse>> getLatestPrice(
            @PathVariable Long coinId) {

        PriceResponse response = priceService.getLatestPrice(coinId);
        return ResponseEntity.ok(ApiResponse.success("최신 가격 조회 성공", response));
    }

    /**
     * GET /api/v1/coins/{coinId}/price/history?page=1&size=20
     * 가격 이력 조회
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<PriceResponse>>> getPriceHistory(
            @PathVariable Long coinId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<PriceResponse> response = priceService.getPriceHistory(coinId, page, size);
        return ResponseEntity.ok(ApiResponse.success("가격 이력 조회 성공", response));
    }

    /**
     * POST /api/v1/coins/{coinId}/price/refresh
     * 수동으로 가격 즉시 갱신 (테스트용)
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refreshPrice() {
        priceService.fetchAndSaveAllPrices();
        return ResponseEntity.ok(ApiResponse.success("가격 갱신 완료"));
    }
}
