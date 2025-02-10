package com.poniansoft.shrtly.user;

import com.poniansoft.shrtly.user.model.UserCreateModel;
import com.poniansoft.shrtly.user.model.UserModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserModel createUser(UserCreateModel userCreateModel) {
        User alreadyExists = userRepository.findByEmail(userCreateModel.getEmail());
        if (alreadyExists != null) {
            throw new UserException("User with email " + userCreateModel.getEmail() + " already exists");
        }

        User user = userMapper.createNewUser(userCreateModel);
        user = userRepository.save(user);
        return userMapper.toUserModel(user);
    }

    @Override
    public User getUserByExternalId(String externalId) {
        User user = userRepository.findByExternalId(externalId);
        if (user == null) {
            throw new UserException("User with external id " + externalId + " not found");
        }
        return user;
    }

    @Override
    public Long getUserCount() {
        return userRepository.count();
    }
}
