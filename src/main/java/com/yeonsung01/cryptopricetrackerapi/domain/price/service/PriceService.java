package com.yeonsung01.cryptopricetrackerapi.domain.price.service;

import com.yeonsung01.cryptopricetrackerapi.domain.price.dto.response.PriceResponse;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;

public interface PriceService {

    // 스케줄러가 호출 — 등록된 코인 전체 가격 갱신
    void fetchAndSaveAllPrices();

    // 특정 코인 최신 가격 조회
    PriceResponse getLatestPrice(Long coinId);

    // 특정 코인 가격 이력 조회
    PageResponse<PriceResponse> getPriceHistory(Long coinId, int page, int size);
}
