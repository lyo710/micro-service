package com.baidu.ub.msoa.example.account.service.impl;

import com.baidu.ub.msoa.example.account.domain.model.Account;
import com.baidu.ub.msoa.example.account.domain.model.User;
import com.baidu.ub.msoa.example.account.domain.repository.UserRepository;
import com.baidu.ub.msoa.example.account.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/20.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserRepository userRepository;

    @Override
    public Account login(String name, String password) {

        for (User user : userRepository.loadAll()) {
            if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                return user;
            }
        }

        return null;
    }
}
