package com.poniansoft.shrtly.click;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ClickRepository extends JpaRepository<Click, Long> {
    @Modifying
    @Query("DELETE FROM Click c WHERE c.clickedAt < :threshold")
    void deleteOldClicks(@Param("threshold") LocalDateTime threshold);
}
