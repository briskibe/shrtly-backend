package com.poniansoft.shrtly.user;

import com.poniansoft.shrtly.user.model.UserCreateModel;
import com.poniansoft.shrtly.user.model.UserModel;

public interface UserService {
    UserModel createUser(UserCreateModel userCreateModel);
    User getUserByExternalId(String externalId);
    Long getUserCount();
}
