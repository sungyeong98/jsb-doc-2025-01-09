package com.ll.sbbdoc20250109.standard.page;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageDto<T> {

    private int currentPage;
    private int pageSize;
    private long totalPages;
    private long totalItems;
    private List<T> items;

    public PageDto(Page<T> page) {
        this.currentPage = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.items = page.getContent();
    }

}
