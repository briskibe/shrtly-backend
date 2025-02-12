package com.poniansoft.shrtly.shopify;

import com.poniansoft.shrtly.base.BaseController;
import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.product.ProductService;
import com.poniansoft.shrtly.shopify.model.ShopifyProduct;
import com.poniansoft.shrtly.shortlink.ShortLinkService;
import com.poniansoft.shrtly.store.Store;
import com.poniansoft.shrtly.store.StoreService;
import com.poniansoft.shrtly.user.User;
import com.poniansoft.shrtly.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://shrtlnk.shop"})
@RequestMapping("/api/shopify")
public class ShopifyController extends BaseController {
    private final ShopifyService shopifyService;
    private final StoreService storeService;
    private final ProductService productService;
    private final ShortLinkService shortLinkService;

    @Value("${shopify.redirect-uri-after-install}")
    private String shopifyRedirectUriAfterInstall;

    public ShopifyController(UserService userService, ShopifyService shopifyService, StoreService storeService, ProductService productService, ShortLinkService shortLinkService) {
        super(userService);
        this.shopifyService = shopifyService;
        this.storeService = storeService;
        this.productService = productService;
        this.shortLinkService = shortLinkService;
    }

    // Step 1: Redirect user to Shopify for authorization
    @GetMapping("/auth")
    public ResponseEntity<Void> authorize(@RequestParam String shop, @RequestParam String userId) {
        String state = userId;
        String shopifyUrl = shopifyService.getAuthorizationUrl(shop,state);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(shopifyUrl)).build();
    }

    // Step 2: Handle OAuth callback
    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code, @RequestParam String shop, @RequestParam String state) {
        String accessToken = shopifyService.exchangeCodeForToken(shop, code);
        User currentUser = userService.getUserByExternalId(state);

        Store store = storeService.addStore(accessToken, "shopify", shop.replace(".myshopify.com", ""), currentUser, shop);

        // Build redirect URI for the frontend
        String redirectUrl = shopifyRedirectUriAfterInstall;
        List<ShopifyProduct> shopifyProducts = shopifyService.fetchProducts(shop, accessToken, 5);
        List<Product> products = productService.createProductsFromShopify(shopifyProducts, store);
        shortLinkService.createShortLinksFromProducts(products, currentUser);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/{storeId}/sync")
    public ResponseEntity<Map<String, Object>> sync(@PathVariable Long storeId, HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        Store store = storeService.getStoreById(storeId);
        if (!store.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<ShopifyProduct> shopifyProducts = shopifyService.syncAllActiveProducts(store);
        List<Product> products = productService.createProductsFromShopify(shopifyProducts, store);
        shortLinkService.createShortLinksFromProducts(products, currentUser);
        storeService.save(store);

        // Summary of sync
        Map<String, Object> response = Map.of(
                "message", "Sync completed successfully",
                "syncedProducts", shopifyProducts.size(),
                "newShortLinks", products.size()
        );

        return ResponseEntity.ok(response);
    }

    // Step 3: Fetch products from Shopify
    /*@GetMapping("/products")
    public ResponseEntity<String> getProducts(@RequestParam String shop, @RequestParam String accessToken) {
        return shopifyService.fetchProducts(shop, accessToken, 5);
    }*/
}
