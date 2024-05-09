package com.aims.datamodel.sdk.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PageResult {
    long total;
    long page;
    long pageSize;
    List<Map<String, Object>> items;

    public PageResult(List<Map<String, Object>> items, long total, long page, long pageSize) {
        this.total = total;
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
    }
}
