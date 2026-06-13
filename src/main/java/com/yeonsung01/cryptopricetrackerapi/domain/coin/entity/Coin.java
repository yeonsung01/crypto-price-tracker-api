package com.yeonsung01.cryptopricetrackerapi.domain.coin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@Getter // getter 자동 생성: coin.getSymbol() 같은 메서드 사용 가능
@Setter // setter 자동 생성: coin.setSymbol("BTC") 같은 메서드 사용 가능
@NoArgsConstructor // 기본 생성자 생성: new Coin()
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
public class Coin {

    private Long id;                  // DB에서 자동 생성되는 coin ID
    private String symbol;            // coin symbol: BTC, ETH, SOL,,
    private String name;              // coin name: Bitcoin, Ethereum
    private String apiId;             // 외부 API 조회용 ID: bitcoin, ethereum, BTCUSDC 등
    private LocalDateTime createdAt;  // 생성 시간
    private LocalDateTime updatedAt;  // 수정 시간

    // 코인 정보 수정 메서드
    public void update(String symbol, String name, String apiId) {
        if (symbol != null && !symbol.trim().isEmpty()) {
            this.symbol =symbol.trim().toUpperCase(); // BTC 처럼 대문자로 저장
        }

        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }

        if(apiId != null && !apiId.trim().isEmpty()) {
            this.apiId = apiId.trim();
        }

        this.updatedAt = LocalDateTime.now(); // 수정 시각 갱신
    }
}
