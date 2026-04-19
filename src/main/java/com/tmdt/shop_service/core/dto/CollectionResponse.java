package com.tmdt.shop_service.core.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CollectionResponse<T> {
    private final List<T> results;
    private final Object nextPageToken;
    private final Long total;

    public CollectionResponse(List<T> data, Object nextPageToken, Long total) {
        this.results = data;
        this.nextPageToken = nextPageToken;
        this.total = total;
    }

    public CollectionResponse(Page<T> page) {
        this.results = page.getContent();
        if (page.hasNext()) {
            this.nextPageToken = page.nextPageable().getPageNumber();
        } else {
            this.nextPageToken = null;
        }
        this.total = page.getTotalElements();
    }
}
