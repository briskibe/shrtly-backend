package com.poniansoft.shrtly.shortlink;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    ShortLink findByShortCode(String shortCode);
}
