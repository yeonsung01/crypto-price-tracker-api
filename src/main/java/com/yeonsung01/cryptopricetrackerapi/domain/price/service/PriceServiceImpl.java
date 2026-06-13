package com.yeonsung01.cryptopricetrackerapi.domain.price.service;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.dao.CoinDao;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.entity.Coin;
import com.yeonsung01.cryptopricetrackerapi.domain.price.dao.PriceHistoryDao;
import com.yeonsung01.cryptopricetrackerapi.domain.price.dto.response.PriceResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.price.entity.PriceHistory;
import com.yeonsung01.cryptopricetrackerapi.global.exception.BusinessException;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CoinGecko 무료 API (무인증)
 * Jackson 3.x 호환을 위해 ObjectMapper 대신 RestTemplate + Map 파싱 사용
 */
@Service
public class PriceServiceImpl implements PriceService {

    private static final String COINGECKO_URL =
            "https://api.coingecko.com/api/v3/simple/price?ids={ids}&vs_currencies=usd";

    private final CoinDao coinDao;
    private final PriceHistoryDao priceHistoryDao;
    private final RestTemplate restTemplate;

    public PriceServiceImpl(CoinDao coinDao,
                            PriceHistoryDao priceHistoryDao,
                            RestTemplate restTemplate) {
        this.coinDao = coinDao;
        this.priceHistoryDao = priceHistoryDao;
        this.restTemplate = restTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fetchAndSaveAllPrices() {
        List<Coin> coins = coinDao.findAll(0, 250);
        if (coins.isEmpty()) return;

        String ids = coins.stream()
                .map(Coin::getApiId)
                .collect(Collectors.joining(","));

        try {
            // RestTemplate이 자동으로 JSON → Map 변환
            // 응답: { "bitcoin": { "usd": 65000.12 }, "ethereum": { "usd": 3100.55 } }
            Map<String, Map<String, Object>> response =
                    restTemplate.getForObject(COINGECKO_URL, Map.class, ids);

            if (response == null) return;

            for (Coin coin : coins) {
                Map<String, Object> coinData = response.get(coin.getApiId());
                if (coinData == null || coinData.get("usd") == null) continue;

                BigDecimal price = new BigDecimal(coinData.get("usd").toString());
                priceHistoryDao.save(new PriceHistory(null, coin.getId(), price, null));
            }

        } catch (Exception e) {
            System.err.println("[PriceService] CoinGecko API 호출 실패: " + e.getMessage());
        }
    }

    @Override
    public PriceResponse getLatestPrice(Long coinId) {
        coinDao.findById(coinId)
                .orElseThrow(() -> BusinessException.notFound("코인을 찾을 수 없습니다. id: " + coinId));

        PriceHistory latest = priceHistoryDao.findLatestByCoinId(coinId)
                .orElseThrow(() -> BusinessException.notFound("아직 가격 데이터가 없습니다. 잠시 후 다시 시도해주세요."));

        return PriceResponse.from(latest);
    }

    @Override
    public PageResponse<PriceResponse> getPriceHistory(Long coinId, int page, int size) {
        coinDao.findById(coinId)
                .orElseThrow(() -> BusinessException.notFound("코인을 찾을 수 없습니다. id: " + coinId));

        if (page < 1) throw BusinessException.badRequest("페이지는 1 이상이어야 합니다.");
        if (size < 1 || size > 100) throw BusinessException.badRequest("size는 1~100 사이여야 합니다.");

        int offset = (page - 1) * size;
        List<PriceResponse> prices = priceHistoryDao.findByCoinId(coinId, offset, size)
                .stream()
                .map(PriceResponse::from)
                .toList();

        long totalCount = priceHistoryDao.countByCoinId(coinId);
        return PageResponse.of(prices, page, size, totalCount);
    }
}
