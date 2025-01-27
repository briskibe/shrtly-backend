package com.poniansoft.shrtly.shopify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ShopifyServiceImpl implements ShopifyService {
    @Value("${shopify.api.key}")
    private String apiKey;

    @Value("${shopify.api.secret}")
    private String apiSecret;

    @Value("${shopify.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // Step 1: Redirect user to Shopify for authorization
    @Override
    public String getAuthorizationUrl(String shopDomain) {
        return String.format(
                "https://%s/admin/oauth/authorize?client_id=%s&scope=read_products,read_orders&redirect_uri=%s",
                shopDomain, apiKey, redirectUri
        );
    }

    // Step 2: Exchange authorization code for access token
    @Override
    public String exchangeCodeForToken(String shopDomain, String code) {
        String url = String.format("https://%s/admin/oauth/access_token", shopDomain);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", apiKey);
        body.add("client_secret", apiSecret);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    // Step 3: Fetch products from Shopify
    @Override
    public ResponseEntity<String> fetchProducts(String shopDomain, String accessToken) {
        String url = String.format("https://%s/admin/api/2023-10/products.json", shopDomain);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
}
