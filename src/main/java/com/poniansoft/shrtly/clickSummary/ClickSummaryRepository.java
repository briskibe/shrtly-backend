package com.poniansoft.shrtly.clickSummary;

import com.poniansoft.shrtly.shortlink.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ClickSummaryRepository extends JpaRepository<ClickSummary, Long> {
    @Modifying
    @Query("UPDATE ClickSummary c SET c.clickCount = c.clickCount + 1 WHERE c.shortLink = :shortLink AND c.date = :date")
    void incrementClickCount(@Param("shortLink") ShortLink shortLink, @Param("date") LocalDate date);

    boolean existsByShortLinkAndDate(ShortLink shortLink, LocalDate date);
}
