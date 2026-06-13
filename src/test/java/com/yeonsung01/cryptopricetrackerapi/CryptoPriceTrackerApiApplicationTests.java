package com.yeonsung01.cryptopricetrackerapi;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.dao.CoinDao;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinCreateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.service.CoinService;
import com.yeonsung01.cryptopricetrackerapi.global.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CryptoPriceTrackerApiApplicationTests {

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinDao coinDao;

    @Test
    @DisplayName("코인 생성 - 정상")
    void createCoin_success() {
        CoinCreateRequest request = new CoinCreateRequest();
        request.setSymbol("btc");
        request.setName("Bitcoin");
        request.setApiId("bitcoin");

        CoinResponse response = coinService.createCoin(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getSymbol()).isEqualTo("BTC"); // 대문자 변환 확인
        assertThat(response.getName()).isEqualTo("Bitcoin");
    }

    @Test
    @DisplayName("코인 생성 - 중복 심볼 예외")
    void createCoin_duplicateSymbol_throwsException() {
        CoinCreateRequest request = new CoinCreateRequest();
        request.setSymbol("ETH");
        request.setName("Ethereum");
        request.setApiId("ethereum");

        coinService.createCoin(request);

        // 동일 심볼 재등록 시 예외 발생 확인
        assertThatThrownBy(() -> coinService.createCoin(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("이미 등록된 코인 심볼");
    }

    @Test
    @DisplayName("코인 단건 조회 - 존재하지 않는 ID 예외")
    void getCoin_notFound_throwsException() {
        assertThatThrownBy(() -> coinService.getCoin(999999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("찾을 수 없습니다");
    }

    @Test
    @DisplayName("스프링 컨텍스트 로드")
    void contextLoads() {
        assertThat(coinService).isNotNull();
        assertThat(coinDao).isNotNull();
    }
}
