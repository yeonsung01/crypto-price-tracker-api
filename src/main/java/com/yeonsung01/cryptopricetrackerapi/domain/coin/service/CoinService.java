package com.yeonsung01.cryptopricetrackerapi.domain.coin.service;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinCreateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinUpdateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinDeleteResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinResponse;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;

/**
 * CoinService 인터페이스
 * - 디자인 패턴: Strategy (인터페이스로 구현체를 교체 가능하게 분리)
 * - 나중에 CoinServiceV2 등으로 구현체만 바꿔도 컨트롤러 코드 변경 없음
 */
public interface CoinService {

    CoinResponse createCoin(CoinCreateRequest request);

    CoinResponse getCoin(Long id);

    PageResponse<CoinResponse> getCoins(int page, int size);

    CoinResponse updateCoin(Long id, CoinUpdateRequest request);

    CoinDeleteResponse deleteCoin(Long id);
}
