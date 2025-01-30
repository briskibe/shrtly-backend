package com.poniansoft.shrtly.store.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreModel {
    Long id;
    String platform;
    String storeName;
    String storeUrl;
    LocalDateTime updatedAt;
}
