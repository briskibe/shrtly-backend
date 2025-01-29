package com.poniansoft.shrtly.shopify;

import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShopifyServiceImpl implements ShopifyService {
    @Value("${shopify.api.key}")
    private String apiKey;

    @Value("${shopify.api.secret}")
    private String apiSecret;

    @Value("${shopify.redirect-uri}")
    private String redirectUri;
    String scopes = "read_products,write_products";

    private final RestTemplate restTemplate = new RestTemplate();

    // Step 1: Redirect user to Shopify for authorization
    @Override
    public String getAuthorizationUrl(String shopDomain, String state) {
        // TODO: check if the shop domain is valid
        // TODO: check if already stored in the database
        String shopifyUrl = String.format(
                "https://%s/admin/oauth/authorize?client_id=%s&scope=%s&redirect_uri=%s&state=%s",
                shopDomain, apiKey, scopes, redirectUri,state
        );
        return shopifyUrl;
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
    public List<ShopifyProduct> fetchProducts(String shopDomain, String accessToken, int limit) {
        String url = String.format("https://%s/admin/api/2023-10/products.json?limit=%s&status=active", shopDomain, limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", accessToken);


        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, Object>> productsData = (List<Map<String, Object>>) response.getBody().get("products");

        return productsData.stream()
                .map(productData -> mapToProduct(productData, shopDomain))
                .collect(Collectors.toList());
    }

    private ShopifyProduct mapToProduct(Map<String, Object> data, String shopDomain) {
        ShopifyProduct product = new ShopifyProduct();
        product.setExternalId((Long) data.get("id"));
        product.setTitle((String) data.get("title"));

        // Construct Storefront URL using handle
        String handle = (String) data.get("handle");
        String productUrl = String.format("https://%s/products/%s", shopDomain, handle);
        product.setUrl(productUrl);

        return product;
    }
}
