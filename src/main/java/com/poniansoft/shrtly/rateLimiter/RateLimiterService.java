package com.poniansoft.shrtly.rateLimiter;

public interface RateLimiterService {
    boolean isAllowed(String ipAddress);
}
