package com.poniansoft.shrtly.analytics.model;

import lombok.Data;

import java.util.List;

@Data
public class ClickAnalyticsDTO {
    private List<ClicksPerDay> clicksOverTime;
    private List<TopShortLinks> topShortLinks;
    private List<ReferrerStats> referrerStats;

    public ClickAnalyticsDTO(List<ClicksPerDay> clicksOverTime,
                             List<TopShortLinks> topShortLinks,
                             List<ReferrerStats> referrerStats) {
        this.clicksOverTime = clicksOverTime;
        this.topShortLinks = topShortLinks;
        this.referrerStats = referrerStats;
    }
}
