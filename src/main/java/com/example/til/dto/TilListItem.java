package com.example.til.dto;

import com.example.til.domain.Til;

import java.time.LocalDateTime;

public class TilListItem {

    private Long id;
    private String title;
    private String tag;
    private LocalDateTime createdAt;

    public TilListItem() {}

    public static TilListItem from(Til til) {
        TilListItem item = new TilListItem();
        item.id = til.getId();
        item.title = til.getTitle();
        item.tag = til.getTag();
        item.createdAt = til.getCreatedAt();
        return item;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getTag() { return tag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
