package com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.entity.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter // getter 자동 생성
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
public class CoinResponse {


    private Long id;                   // 코인 ID
    private String symbol;             // 코인 심볼, BTC, ETH
    private String name;               // 코인 이름, ex) Bitcoin
    private String apiId;              // 외부 API 조회용 ID
    private LocalDateTime createdAt;   // 생성 시간
    private LocalDateTime updatedAt;   // 수정 시간

    // Entity -> Response DTO 변환 메서드
    public  static CoinResponse from(Coin coin) {

        return new CoinResponse(
                coin.getId(),
                coin.getSymbol(),
                coin.getName(),
                coin.getApiId(),
                coin.getCreatedAt(),
                coin.getUpdatedAt()
        );
    }
}
