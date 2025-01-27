package com.poniansoft.shrtly.user;

import com.poniansoft.shrtly.store.StoreMapper;
import com.poniansoft.shrtly.user.model.UserCreateModel;
import com.poniansoft.shrtly.user.model.UserModel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final StoreMapper storeMapper;

    public UserMapper(StoreMapper storeMapper) {
        this.storeMapper = storeMapper;
    }

    public User createNewUser(UserCreateModel userCreateModel) {
        User user = new User();
        user.setExternalId(userCreateModel.getUid());
        user.setEmail(userCreateModel.getEmail());
        user.setActive(false);
        return user;
    }

    public UserModel toUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setExternalId(user.getExternalId());
        userModel.setEmail(user.getEmail());
        if (user.getStores() != null) {
            userModel.setStores(user.getStores().stream().map(storeMapper::toStoreModel).collect(Collectors.toList()));
        }
        return userModel;
    }
}
