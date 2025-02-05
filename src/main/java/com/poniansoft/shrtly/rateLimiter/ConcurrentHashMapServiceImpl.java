package com.poniansoft.shrtly.rateLimiter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
public class ConcurrentHashMapServiceImpl implements RateLimiterService {
    private final Map<String, RequestRecord> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 10;

    @Override
    public boolean isAllowed(String ipAddress) {
        long currentTimeMillis = Instant.now().toEpochMilli();
        requestCounts.compute(ipAddress, (key, record) -> {
            if (record == null || (currentTimeMillis - record.getTimestamp()) > 60000) {
                // Reset if no record or the record is older than 1 minute
                return new RequestRecord(1, currentTimeMillis);
            } else {
                // Increment request count if within the time window
                record.incrementRequestCount();
                return record;
            }
        });

        RequestRecord currentRecord = requestCounts.get(ipAddress);
        return currentRecord.getRequestCount() <= MAX_REQUESTS_PER_MINUTE;
    }

    // Helper class to track request count and timestamp
    private static class RequestRecord {
        private int requestCount;
        private long timestamp;

        public RequestRecord(int requestCount, long timestamp) {
            this.requestCount = requestCount;
            this.timestamp = timestamp;
        }

        public int getRequestCount() {
            return requestCount;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void incrementRequestCount() {
            this.requestCount++;
        }
    }
}
