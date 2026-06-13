package com.yeonsung01.cryptopricetrackerapi.domain.price.dao;

import com.yeonsung01.cryptopricetrackerapi.domain.price.entity.PriceHistory;

import java.util.List;
import java.util.Optional;

public interface PriceHistoryDao {

    PriceHistory save(PriceHistory priceHistory);

    // 특정 코인의 최신 가격 1개
    Optional<PriceHistory> findLatestByCoinId(Long coinId);

    // 특정 코인의 가격 이력 (최신순)
    List<PriceHistory> findByCoinId(Long coinId, int offset, int limit);

    long countByCoinId(Long coinId);
}
