package com.poniansoft.shrtly.clickSummary;

import com.poniansoft.shrtly.shortlink.ShortLink;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "click_summary", indexes = {
        @Index(name = "idx_click_summary_short_link_id", columnList = "short_link_id")
})
@Data
public class ClickSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "click_sum_id_generator")
    @SequenceGenerator(name = "click_sum_id_generator", sequenceName = "CLICK_SUM_SEQUENCE", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "short_link_id", nullable = false)
    private ShortLink shortLink;

    @Column(name = "short_code", nullable = false, length = 50)
    private String shortCode;

    @Column(name = "slug", nullable = false, length = 50)
    private String slug;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int clickCount = 0;
}
