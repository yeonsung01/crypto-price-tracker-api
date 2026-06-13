package com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 생성
public class CoinCreateRequest {

    // 코인 심볼 ex) BTC, ETH, SOL
    @NotBlank(message = "코인 심볼은 필수입니다.")
    private String symbol;

    // 코인 이름 ex) Bitcoin, Ethereum
    @NotBlank(message = "코인 이름은 필수입니다.")
    private  String name;

    // 외부 Api 조회용 ID ex) bitcoin, ethereum
    @NotBlank(message = "API ID는 필수입니다" )
    private String apiId;

}
