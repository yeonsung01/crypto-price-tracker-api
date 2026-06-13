package com.yeonsung01.cryptopricetrackerapi.domain.coin.service;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.dao.CoinDao;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinCreateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.request.CoinUpdateRequest;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinDeleteResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.dto.response.CoinResponse;
import com.yeonsung01.cryptopricetrackerapi.domain.coin.entity.Coin;
import com.yeonsung01.cryptopricetrackerapi.global.exception.BusinessException;
import com.yeonsung01.cryptopricetrackerapi.global.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CoinService 구현체
 * - 비즈니스 로직 집중 (DB 접근은 DAO에 위임)
 * - 디자인 패턴: Template Method 구조 (createCoin: 검증 → 변환 → 저장 → 반환 흐름 고정)
 */
@Service
public class CoinServiceImpl implements CoinService {

    private final CoinDao coinDao;

    public CoinServiceImpl(CoinDao coinDao) {
        this.coinDao = coinDao;
    }

    @Override
    public CoinResponse createCoin(CoinCreateRequest request) {
        // 1. 중복 심볼 검증
        String upperSymbol = request.getSymbol().trim().toUpperCase();
        if (coinDao.existsBySymbol(upperSymbol)) {
            throw BusinessException.conflict("이미 등록된 코인 심볼입니다: " + upperSymbol);
        }

        // 2. Entity 생성
        Coin coin = new Coin(
                null,
                upperSymbol,
                request.getName().trim(),
                request.getApiId().trim(),
                null,
                null
        );

        // 3. 저장 후 응답 반환
        Coin savedCoin = coinDao.save(coin);
        return CoinResponse.from(savedCoin);
    }

    @Override
    public CoinResponse getCoin(Long id) {
        Coin coin = coinDao.findById(id)
                .orElseThrow(() -> BusinessException.notFound("코인을 찾을 수 없습니다. id: " + id));

        return CoinResponse.from(coin);
    }

    @Override
    public PageResponse<CoinResponse> getCoins(int page, int size) {
        // 페이지 유효성 검증
        if (page < 1) throw BusinessException.badRequest("페이지는 1 이상이어야 합니다.");
        if (size < 1 || size > 100) throw BusinessException.badRequest("size는 1~100 사이여야 합니다.");

        int offset = (page - 1) * size;
        List<CoinResponse> coins = coinDao.findAll(offset, size)
                .stream()
                .map(CoinResponse::from)
                .toList();

        long totalCount = coinDao.count();
        return PageResponse.of(coins, page, size, totalCount);
    }

    @Override
    public CoinResponse updateCoin(Long id, CoinUpdateRequest request) {
        // 1. 코인 존재 확인
        Coin coin = coinDao.findById(id)
                .orElseThrow(() -> BusinessException.notFound("코인을 찾을 수 없습니다. id: " + id));

        // 2. 심볼 변경 시 중복 확인 (현재 코인 자신의 심볼이 아닌 경우만)
        if (request.getSymbol() != null && !request.getSymbol().isBlank()) {
            String newSymbol = request.getSymbol().trim().toUpperCase();
            if (!newSymbol.equals(coin.getSymbol()) && coinDao.existsBySymbol(newSymbol)) {
                throw BusinessException.conflict("이미 사용 중인 코인 심볼입니다: " + newSymbol);
            }
        }

        // 3. Entity 수정 (null이면 기존값 유지 - Coin.update() 메서드가 처리)
        coin.update(request.getSymbol(), request.getName(), request.getApiId());

        // 4. DB 업데이트 후 반환
        Coin updatedCoin = coinDao.update(coin);
        return CoinResponse.from(updatedCoin);
    }

    @Override
    public CoinDeleteResponse deleteCoin(Long id) {
        // 존재 확인
        coinDao.findById(id)
                .orElseThrow(() -> BusinessException.notFound("코인을 찾을 수 없습니다. id: " + id));

        coinDao.deleteById(id);
        return CoinDeleteResponse.of(id);
    }
}
