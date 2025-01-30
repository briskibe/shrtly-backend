package com.poniansoft.shrtly.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClicksPerDay {
    private LocalDate date;
    private Long totalClicks;
}
