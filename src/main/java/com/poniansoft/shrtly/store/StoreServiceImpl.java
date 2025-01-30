package com.poniansoft.shrtly.store;

import com.poniansoft.shrtly.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional
    @Override
    public Store addStore(String accessToken, String platform, String storeName, User user, String storeUrl) {
        Store store = new Store();
        store.setAccessToken(accessToken);
        store.setPlatform(platform);
        store.setStoreName(storeName);
        store.setUser(user);
        store.setStoreUrl(storeUrl);

        return storeRepository.save(store);
    }

    @Override
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
    }
}
