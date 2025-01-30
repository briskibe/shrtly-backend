package com.poniansoft.shrtly.shopify;

import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.store.Store;

import java.util.List;

public interface ShopifyService {
    // Step 1: Redirect user to Shopify for authorization
    String getAuthorizationUrl(String shopDomain, String state);

    String exchangeCodeForToken(String shopDomain, String code);

    // Step 3: Fetch products from Shopify
    List<ShopifyProduct> fetchProducts(String shopDomain, String accessToken, int limit);

    List<ShopifyProduct> syncAllActiveProducts(Store store);
}
