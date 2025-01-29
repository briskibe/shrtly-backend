package com.poniansoft.shrtly.click;

import com.poniansoft.shrtly.clickSummary.ClickSummary;
import com.poniansoft.shrtly.clickSummary.ClickSummaryRepository;
import com.poniansoft.shrtly.shortlink.ShortLink;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ClickServiceImpl implements ClickService {
    private final ClickRepository clickRepository;
    private final ClickSummaryRepository clickSummaryRepository;

    public ClickServiceImpl(ClickRepository clickRepository, ClickSummaryRepository clickSummaryRepository) {
        this.clickRepository = clickRepository;
        this.clickSummaryRepository = clickSummaryRepository;
    }

    @Transactional
    public void trackClick(ShortLink shortLink, String ipAddress, String userAgent, String referrer) {
        LocalDate today = LocalDate.now();

        // 1️⃣ Store detailed click (short-term)
        Click click = new Click();
        click.setShortLink(shortLink);
        click.setClickedAt(LocalDateTime.now());
        click.setIpAddress(ipAddress);
        click.setUserAgent(userAgent);
        click.setReferrer(referrer);
        clickRepository.save(click);

        // 2️⃣ Update daily click count (long-term)
        if (clickSummaryRepository.existsByShortLinkAndDate(shortLink, today)) {
            clickSummaryRepository.incrementClickCount(shortLink, today);
        } else {
            ClickSummary summary = new ClickSummary();
            summary.setShortLink(shortLink);
            summary.setDate(today);
            summary.setClickCount(1);
            clickSummaryRepository.save(summary);
        }
    }

    // 3️⃣ Automatically delete clicks older than 30 days
    @Scheduled(cron = "0 0 3 * * ?") // Runs daily at 3 AM
    @Transactional
    public void cleanOldClicks() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        clickRepository.deleteOldClicks(threshold);
    }
}
