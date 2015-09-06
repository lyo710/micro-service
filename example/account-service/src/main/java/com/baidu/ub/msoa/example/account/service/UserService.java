package com.baidu.ub.msoa.example.account.service;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.CollectionElementType;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.example.account.domain.model.User;

import java.util.List;

/**
 * Created by pippo on 15/8/20.
 */
@BundleService(name = "userService", version = 1)
public interface UserService {

    /**
     * get user by id
     *
     * @param id
     * @return user
     */
    User getById(long id);

    /**
     * load all user
     *
     * @return users
     */
    @CollectionElementType(User.class)
    List<User> loadAll();

    /**
     * save user
     *
     * @param user
     */
    void save(User user);

    /**
     * replace user
     *
     * @param id
     * @param user
     * @return user
     */
    User replace(long id, User user);

}
