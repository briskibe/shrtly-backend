package com.poniansoft.shrtly.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopShortLinks {
    private String shortUrl;
    private Long totalClicks;
}
