package com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoinDeleteResponse {

    private String message;    // 삭제 완료 메시지
    private Long deleteId;     // 삭제된 코인 ID

    // private -> public 으로 수정 (Service에서 호출하므로)
    public static CoinDeleteResponse of(Long id) {
        return new CoinDeleteResponse("코인이 성공적으로 삭제되었습니다.", id);
    }
}
