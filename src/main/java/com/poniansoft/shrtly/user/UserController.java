package com.poniansoft.shrtly.user;

import com.poniansoft.shrtly.base.BaseController;
import com.poniansoft.shrtly.user.model.UserCreateModel;
import com.poniansoft.shrtly.user.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        super(userService);
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public UserModel createUser(@Valid @RequestBody UserCreateModel userCreateModel) {
        return userService.createUser(userCreateModel);
    }

    @GetMapping("/{externalId}")
    public UserModel getUserById(@PathVariable String externalId) {
        User user = userService.getUserByExternalId(externalId);
        return userMapper.toUserModel(user);
    }

    @GetMapping("/current")
    public UserModel getCurrentUserModel(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return userMapper.toUserModel(user);
    }
}
