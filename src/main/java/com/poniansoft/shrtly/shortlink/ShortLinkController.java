package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.click.ClickService;
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

    public ShortLinkController(ShortLinkService shortLinkService, ClickService clickService) {
        this.shortLinkService = shortLinkService;
        this.clickService = clickService;
    }

    // Short link redirect handler (e.g., https://shrtlnk.shop/abc123)
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> handleShortLink(@PathVariable String shortCode, HttpServletRequest request) {
        ShortLink shortLink = shortLinkService.findByShortCode(shortCode);
        if (shortLink == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Track the click
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");
        clickService.trackClick(shortLink, ipAddress, userAgent, referrer);

        // Redirect to the actual product URL
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", shortLink.getLongUrl()).build();
    }
}
