package com.yeonsung01.cryptopricetrackerapi.domain.price.scheduler;

import com.yeonsung01.cryptopricetrackerapi.domain.price.service.PriceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 가격 자동 조회 스케줄러
 * - @EnableScheduling 은 AppConfig 에서 활성화
 * - fixedDelay: 이전 실행 완료 후 5분 대기 (API 응답 느릴 때 중복 호출 방지)
 */
@Component
public class PriceScheduler {

    private final PriceService priceService;

    public PriceScheduler(PriceService priceService) {
        this.priceService = priceService;
    }

    // 앱 시작 후 1분 뒤 첫 실행, 이후 5분마다 반복
    @Scheduled(initialDelayString = "60000", fixedDelayString = "300000")
    public void scheduleFetchPrices() {
        System.out.println("[Scheduler] 가격 조회 시작: " + LocalDateTime.now());
        priceService.fetchAndSaveAllPrices();
        System.out.println("[Scheduler] 가격 조회 완료: " + LocalDateTime.now());
    }
}
