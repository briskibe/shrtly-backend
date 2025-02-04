package com.poniansoft.shrtly.analytics;

import com.poniansoft.shrtly.analytics.model.ClickAnalyticsDTO;
import com.poniansoft.shrtly.analytics.model.ClicksPerDay;
import com.poniansoft.shrtly.analytics.model.ReferrerStats;
import com.poniansoft.shrtly.analytics.model.TopShortLinks;
import com.poniansoft.shrtly.click.ClickRepository;
import com.poniansoft.shrtly.clickSummary.ClickSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {
    private final ClickRepository clickRepository;
    private final ClickSummaryRepository clickSummaryRepository;

    public AnalyticsServiceImpl(ClickRepository clickRepository, ClickSummaryRepository clickSummaryRepository) {
        this.clickRepository = clickRepository;
        this.clickSummaryRepository = clickSummaryRepository;
    }

    @Override
    public ClickAnalyticsDTO getAnalytics(Long storeId) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(30);
        List<ClicksPerDay> clicksOverTime = clickSummaryRepository.findClicksOverTime(sevenDaysAgo, storeId);
        List<TopShortLinks> topShortLinks = clickSummaryRepository.findTopShortLinks(storeId);
        List<ReferrerStats> referrerStats = clickRepository.findReferrerStats(storeId);

        return new ClickAnalyticsDTO(clicksOverTime, topShortLinks, referrerStats);
    }
}
