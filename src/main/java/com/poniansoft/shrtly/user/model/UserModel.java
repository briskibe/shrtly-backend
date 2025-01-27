package com.poniansoft.shrtly.user.model;

import com.poniansoft.shrtly.store.model.StoreModel;
import lombok.Data;

import java.util.List;

@Data
public class UserModel {
    Long id;
    String externalId;
    String email;
    List<StoreModel> stores;
}
