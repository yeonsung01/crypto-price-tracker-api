package com.yeonsung01.cryptopricetrackerapi.domain.coin.dao;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.entity.Coin;

import java.util.Optional;
import java.util.List;

public interface CoinDao {

    Coin save(Coin coin);                     // 코인 저장
    Optional<Coin> findById(Long id);         // 코인 단건 조회
    List<Coin> findAll(int offset, int limit);
    long count();                             // 전체 코인 개수 조회
    boolean existsBySymbol(String symbol);    // 심볼 중복 확인
    Coin update(Coin coin);                   // 코인 수정
    boolean deleteById(Long id);              // 코인 삭제

}
