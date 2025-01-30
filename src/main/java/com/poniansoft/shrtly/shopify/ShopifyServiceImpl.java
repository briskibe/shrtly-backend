package com.poniansoft.shrtly.shopify;

import com.poniansoft.shrtly.product.ProductRepository;
import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.shortlink.ShortLinkService;
import com.poniansoft.shrtly.store.Store;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final ShortLinkService shortLinkService;
    private final ProductRepository productRepository;

    public ShopifyServiceImpl(ShortLinkService shortLinkService, ProductRepository productRepository) {
        this.shortLinkService = shortLinkService;
        this.productRepository = productRepository;
    }

    // Step 1: Redirect user to Shopify for authorization
    @Override
    public String getAuthorizationUrl(String shopDomain, String state) {
        // TODO: check if the shop domain is valid
        // TODO: check if already stored in the database
        String shopifyUrl = String.format(
                "https://%s/admin/oauth/authorize?client_id=%s&scope=%s&redirect_uri=%s&state=%s",
                shopDomain, apiKey, scopes, redirectUri, state
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

    @Override
    @Transactional
    public List<ShopifyProduct> syncAllActiveProducts(Store store) {
        String endpoint = String.format("https://%s/admin/api/2023-01/products.json?limit=50&status=active", store.getStoreUrl());
        List<ShopifyProduct> allProducts = new ArrayList<>();
        boolean hasNextPage = true;
        String nextPageUrl = endpoint;

        while (hasNextPage) {
            try {
                // Make API call to fetch products
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-Shopify-Access-Token", store.getAccessToken());


                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<Map> response = restTemplate.exchange(nextPageUrl, HttpMethod.GET, entity, Map.class);

                List<Map<String, Object>> products = (List<Map<String, Object>>) response.getBody().get("products");

                if (products == null || products.isEmpty()) {
                    break;  // No more products to process
                }

                // Convert API response to ShopifyProduct objects
                for (Map<String, Object> productData : products) {
                    Long productId = (Long) productData.get("id");
                    String productTitle = (String) productData.get("title");
                    String productHandle = (String) productData.get("handle");

                    allProducts.add(new ShopifyProduct(productId, productTitle, productHandle));
                }

                // Check for next page in response headers
                String linkHeader = extractNextPageUrl(response.getHeaders().get("Link"));
                if (linkHeader == null) {
                    hasNextPage = false;
                } else {
                    nextPageUrl = linkHeader;  // Update nextPageUrl with the next page info
                }

            } catch (Exception e) {
                e.printStackTrace();
                hasNextPage = false;  // Stop syncing if an error occurs
            }
        }

        return allProducts;
    }

    // Helper method to extract the "next" page URL from Link header
    private String extractNextPageUrl(List<String> linkHeaders) {
        if (linkHeaders != null) {
            for (String link : linkHeaders) {
                if (link.contains("rel=\"next\"")) {
                    int start = link.indexOf("<") + 1;
                    int end = link.indexOf(">");
                    return link.substring(start, end);  // Return the next page URL
                }
            }
        }
        return null;  // No more pages
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
