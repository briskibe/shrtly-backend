package com.poniansoft.shrtly.shopify;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopify")
public class ShopifyController {
    private final ShopifyService shopifyService;

    public ShopifyController(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    // Step 1: Redirect user to Shopify for authorization
    @GetMapping("/auth")
    public String authorize(@RequestParam String shop) {
        return "redirect:" + shopifyService.getAuthorizationUrl(shop);
    }

    // Step 2: Handle OAuth callback
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code, @RequestParam String shop) {
        String accessToken = shopifyService.exchangeCodeForToken(shop, code);
        return ResponseEntity.ok("Access Token: " + accessToken);
    }

    // Step 3: Fetch products from Shopify
    @GetMapping("/products")
    public ResponseEntity<String> getProducts(@RequestParam String shop, @RequestParam String accessToken) {
        return shopifyService.fetchProducts(shop, accessToken);
    }
}
