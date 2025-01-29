package com.poniansoft.shrtly.click;

import com.poniansoft.shrtly.shortlink.ShortLink;

public interface ClickService {
    void cleanOldClicks();
    void trackClick(ShortLink shortLink, String ipAddress, String userAgent, String referrer);
}
