package com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoinUpdateRequest {

    private String symbol;       // 수정 시 선택 입력, null이면 수정하지 않음
    private String name;
    private String apiId;
}
