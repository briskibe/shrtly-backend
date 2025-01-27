package com.poniansoft.shrtly.store;

import com.poniansoft.shrtly.store.model.StoreModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class StoreMapper {
    public StoreModel toStoreModel(Store store) {
        StoreModel storeModel = new StoreModel();
        storeModel.setId(store.getId());
        storeModel.setPlatform(store.getPlatform());
        storeModel.setStoreName(store.getStoreName());
        storeModel.setStoreUrl(store.getStoreUrl());
        return storeModel;
    }
}
