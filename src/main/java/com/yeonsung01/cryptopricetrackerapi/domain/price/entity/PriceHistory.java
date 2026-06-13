package com.yeonsung01.cryptopricetrackerapi.domain.price.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {

    private Long id;
    private Long coinId;
    private BigDecimal priceUsd;   // DECIMAL(20,8) — 소수점 8자리까지
    private LocalDateTime fetchedAt;
}
