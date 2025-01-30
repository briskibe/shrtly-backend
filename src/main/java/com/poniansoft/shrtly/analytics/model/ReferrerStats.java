package com.poniansoft.shrtly.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReferrerStats {
    private String referrer;
    private Long count;
}
