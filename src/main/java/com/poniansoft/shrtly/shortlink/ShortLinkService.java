package com.poniansoft.shrtly.shortlink;

import com.poniansoft.shrtly.product.Product;
import com.poniansoft.shrtly.user.User;

import java.util.List;

public interface ShortLinkService {
    void createShortLinksFromProducts(List<Product> productList, User user);
    ShortLink findByShortCode(String shortCode, String slug);
}
