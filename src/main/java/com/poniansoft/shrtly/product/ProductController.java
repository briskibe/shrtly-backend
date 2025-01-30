package com.poniansoft.shrtly.product;

import com.poniansoft.shrtly.base.BaseController;
import com.poniansoft.shrtly.product.model.ProductShortLink;
import com.poniansoft.shrtly.store.Store;
import com.poniansoft.shrtly.store.StoreService;
import com.poniansoft.shrtly.user.User;
import com.poniansoft.shrtly.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api/store/{storeId}/products")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final StoreService storeService;

    public ProductController(ProductService productService, UserService userService, StoreService storeService) {
        super(userService);
        this.productService = productService;
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductShortLink>> getProducts(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        Store store = storeService.getStoreById(storeId);
        if (!store.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        Page<ProductShortLink> productPage = productService.getProductsWithShortLinks(storeId, page, size);
        return ResponseEntity.ok(productPage);
    }
}
