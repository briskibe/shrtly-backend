package com.poniansoft.shrtly.store;

import com.poniansoft.shrtly.user.User;
import org.springframework.transaction.annotation.Transactional;

public interface StoreService {
    Store addStore(String accessToken, String platform, String storeName, User user, String storeUrl);
}
