package com.poniansoft.shrtly.base;

import com.poniansoft.shrtly.user.User;
import com.poniansoft.shrtly.user.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class BaseController {
    protected final UserService userService;

    public BaseController(UserService userService) {
        this.userService = userService;
    }

    public User getCurrentUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User ID not found in request");
        }

        User user = userService.getUserByExternalId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }
}
