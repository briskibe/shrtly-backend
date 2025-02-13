package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.click.ClickService;
import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.rateLimiter.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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
    public ResponseEntity<String> handleShortLink(@PathVariable String shortLinkSlug, @PathVariable String shortCode, HttpServletRequest request) {
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

        Product product = shortLink.getProduct(); // Get associated product

        // Check if request is from a social media crawler
        String userAgent = request.getHeader("User-Agent");
        boolean isCrawler = userAgent != null && Arrays.asList(
                "facebookexternalhit",  // Facebook
                "twitterbot",           // Twitter
                "linkedinbot",          // LinkedIn
                "googlebot"            // Google (for SEO)
        ).stream().anyMatch(crawler -> userAgent.toLowerCase().contains(crawler));

        if (isCrawler) {
            // Serve an HTML page with Open Graph and Twitter meta tags
            String previewHtml = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta property="og:title" content="%s">
                <meta property="og:description" content="Check out this product: %s">
                <meta property="og:image" content="%s">
                <meta property="og:url" content="%s">
                <meta property="og:type" content="product">
                
                <meta name="twitter:card" content="summary_large_image">
                <meta name="twitter:title" content="%s">
                <meta name="twitter:description" content="Check out this product: %s">
                <meta name="twitter:image" content="%s">
            </head>
            <body>
            Redirecting...
            </body>
            </html>
        """.formatted(
                    product.getProductName(),
                    product.getProductName(),
                    product.getProductImageUrl(),
                    shortLink.getLongUrl(),
                    product.getProductName(),
                    product.getProductName(),
                    product.getProductImageUrl(),
                    shortLink.getLongUrl()
            );

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(previewHtml);
        }

        // Track the click only for normal users (not bots)
        String referrer = request.getHeader("Referer");
        clickService.trackClick(shortLink, ipAddress, userAgent, referrer);

        // Redirect to actual product URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", shortLink.getLongUrl())
                .build();
    }
}
