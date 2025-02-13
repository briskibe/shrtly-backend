package com.poniansoft.shrtly.click;

import com.poniansoft.shrtly.analytics.model.ClickAnalyticsDTO;
import com.poniansoft.shrtly.analytics.model.ReferrerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClickRepository extends JpaRepository<Click, Long> {
    @Modifying
    @Query("DELETE FROM Click c WHERE c.clickedAt < :threshold")
    void deleteOldClicks(@Param("threshold") LocalDateTime threshold);

    @Query("""
    SELECT new com.poniansoft.shrtly.analytics.model.ReferrerStats(
        COALESCE(c.referrer, 'Direct Click'), COUNT(*))
    FROM Click c
    JOIN c.shortLink sl
    JOIN sl.product p
    JOIN p.store s
    WHERE s.id = :storeId
    GROUP BY COALESCE(c.referrer, 'Direct Click')
    ORDER BY COUNT(*) DESC
""")
    List<ReferrerStats> findReferrerStats(@Param("storeId") Long storeId);
}
