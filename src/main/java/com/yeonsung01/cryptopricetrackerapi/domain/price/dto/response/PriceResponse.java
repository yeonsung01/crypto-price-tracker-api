package com.yeonsung01.cryptopricetrackerapi.domain.price.dto.response;

import com.yeonsung01.cryptopricetrackerapi.domain.price.entity.PriceHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PriceResponse {

    private Long coinId;
    private BigDecimal priceUsd;
    private LocalDateTime fetchedAt;

    public static PriceResponse from(PriceHistory priceHistory) {
        return new PriceResponse(
                priceHistory.getCoinId(),
                priceHistory.getPriceUsd(),
                priceHistory.getFetchedAt()
        );
    }
}
