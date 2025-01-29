package com.poniansoft.shrtly.shopify;

import org.springframework.http.ResponseEntity;

public interface ShopifyService {
    // Step 1: Redirect user to Shopify for authorization
    String getAuthorizationUrl(String shopDomain, String state);

    String exchangeCodeForToken(String shopDomain, String code);

    // Step 3: Fetch products from Shopify
    ResponseEntity<String> fetchProducts(String shopDomain, String accessToken);
}
