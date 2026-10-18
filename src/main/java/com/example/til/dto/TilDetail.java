package com.example.til.dto;

import com.example.til.domain.Til;

import java.time.LocalDateTime;

public class TilDetail {

    private Long id;
    private String title;
    private String content;
    private String tag;
    private LocalDateTime createdAt;

    public TilDetail() {}

    public static TilDetail from(Til til) {
        TilDetail detail = new TilDetail();
        detail.id = til.getId();
        detail.title = til.getTitle();
        detail.content = til.getContent();
        detail.tag = til.getTag();
        detail.createdAt = til.getCreatedAt();
        return detail;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTag() { return tag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
