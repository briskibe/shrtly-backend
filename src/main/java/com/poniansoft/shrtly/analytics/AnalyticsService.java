package com.poniansoft.shrtly.analytics;

import com.poniansoft.shrtly.analytics.model.ClickAnalyticsDTO;

public interface AnalyticsService {
    ClickAnalyticsDTO getAnalytics(Long storeId);
}
