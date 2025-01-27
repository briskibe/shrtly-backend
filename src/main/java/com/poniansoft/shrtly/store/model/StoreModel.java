package com.poniansoft.shrtly.store.model;

import lombok.Data;

@Data
public class StoreModel {
    Long id;
    String platform;
    String storeName;
    String storeUrl;
}
