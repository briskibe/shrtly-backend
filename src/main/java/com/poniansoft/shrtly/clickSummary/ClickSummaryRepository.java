package com.poniansoft.shrtly.clickSummary;

import com.poniansoft.shrtly.analytics.model.ClickAnalyticsDTO;
import com.poniansoft.shrtly.analytics.model.ClicksPerDay;
import com.poniansoft.shrtly.analytics.model.TopShortLinks;
import com.poniansoft.shrtly.shortlink.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClickSummaryRepository extends JpaRepository<ClickSummary, Long> {
    @Modifying
    @Query("UPDATE ClickSummary c SET c.clickCount = c.clickCount + 1 WHERE c.shortLink = :shortLink AND c.date = :date and c.slug = :slug and c.shortCode = :shortCode")
    void incrementClickCount(@Param("shortLink") ShortLink shortLink, @Param("date") LocalDate date, @Param("slug") String slug, @Param("shortCode") String shortCode);

    boolean existsByShortLinkAndDateAndSlugAndShortCode(ShortLink shortLink, LocalDate date, String slug, String shortCode);

    @Query("""
        SELECT new com.poniansoft.shrtly.analytics.model.ClicksPerDay(
            c.date, SUM(c.clickCount))
        FROM ClickSummary c
        left join c.shortLink sl
        left join sl.product p
        left join p.store s
        WHERE s.id = :storeId and c.date >= :startDate
        GROUP BY c.date
        ORDER BY c.date ASC
    """)
    List<ClicksPerDay> findClicksOverTime(@Param("startDate") LocalDate startDate, @Param("storeId") Long storeId);

    @Query("""
        SELECT new com.poniansoft.shrtly.analytics.model.TopShortLinks(
            concat(c.slug, '/', c.shortCode), SUM(c.clickCount))
        FROM ShortLink sl
        left join sl.product p
        left join p.store s
        JOIN ClickSummary c ON sl.id = c.shortLink.id
        where s.id = :storeId
        GROUP BY c.slug, c.shortCode
        ORDER BY SUM(c.clickCount) DESC
        LIMIT 5
    """)
    List<TopShortLinks> findTopShortLinks(@Param("storeId") Long storeId);

    @Query("select cs from ClickSummary cs left join cs.shortLink sl left join sl.product p where p.id = :productId")
    List<ClickSummary> findByProductId(Long productId);
}
