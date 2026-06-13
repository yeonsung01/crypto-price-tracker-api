package com.yeonsung01.cryptopricetrackerapi.global.response;

import lombok.Getter;

import java.util.List;

/**
 * 페이지네이션 응답 래퍼
 */
@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;
    private final boolean hasNext;
    private final boolean hasPrevious;

    private PageResponse(List<T> content, int page, int size, long totalCount) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / size);
        this.hasNext = page < this.totalPages;
        this.hasPrevious = page > 1;
    }

    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalCount) {
        return new PageResponse<>(content, page, size, totalCount);
    }
}
