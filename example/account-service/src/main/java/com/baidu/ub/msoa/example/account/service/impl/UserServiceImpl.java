package com.baidu.ub.msoa.example.account.service.impl;

import com.baidu.ub.msoa.example.account.domain.model.User;
import com.baidu.ub.msoa.example.account.domain.repository.UserRepository;
import com.baidu.ub.msoa.example.account.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public User getById(long id) {

        for (User user : loadAll()) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> loadAll() {
        return new ArrayList<>(userRepository.loadAll());
    }

    @Override
    public void save(User user) {
        userRepository.addUser(user);
    }

    @Override
    public User replace(long id, User user) {
        User exist = getById(id);

        if (exist != null) {
            userRepository.remove(exist);
        }

        user.setId(id);
        userRepository.addUser(user);
        return user;
    }

}
