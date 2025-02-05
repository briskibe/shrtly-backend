package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.click.ClickService;
import com.poniansoft.shrtly.rateLimiter.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ShortLinkController {
    private final ShortLinkService shortLinkService;
    private final ClickService clickService;
    private final RateLimiterService rateLimiterService;

    public ShortLinkController(ShortLinkService shortLinkService, ClickService clickService, RateLimiterService rateLimiterService) {
        this.shortLinkService = shortLinkService;
        this.clickService = clickService;
        this.rateLimiterService = rateLimiterService;
    }

    // Short link redirect handler (e.g., https://shrtlnk.shop/abc123)
    @GetMapping("{shortLinkSlug}/{shortCode}")
    public ResponseEntity<Void> handleShortLink(@PathVariable String shortLinkSlug, @PathVariable String shortCode, HttpServletRequest request) {
        if (shortLinkSlug == null || shortCode == null || shortLinkSlug.equals("api")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Get IP address of the client
        String ipAddress = request.getRemoteAddr();

        // Check rate limit
        if (!rateLimiterService.isAllowed(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();  // 429 Too Many Requests
        }

        ShortLink shortLink = shortLinkService.findByShortCode(shortCode, shortLinkSlug);
        if (shortLink == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Track the click
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");
        clickService.trackClick(shortLink, ipAddress, userAgent, referrer);

        // Redirect to the actual product URL
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", shortLink.getLongUrl()).build();
    }
}
