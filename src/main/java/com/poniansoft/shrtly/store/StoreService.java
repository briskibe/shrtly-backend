package com.poniansoft.shrtly.store;

import com.poniansoft.shrtly.user.User;

public interface StoreService {
    Store addStore(String accessToken, String platform, String storeName, User user, String storeUrl);
    Store getStoreById(Long storeId);
}
