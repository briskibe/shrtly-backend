package com.poniansoft.shrtly.click.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoricalClickDTO {
    private String slug;
    private String shortCode;
    private int totalClicks;

    public void addClicks(int clicks) {
        this.totalClicks += clicks;
    }
}
