package com.baidu.ub.msoa.example.account.domain.repository;

import com.baidu.ub.msoa.example.account.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
@Repository
public class UserRepository {

    private static Set<User> users = new HashSet<>();

    /**
     * add user
     *
     * @param user
     */
    public void addUser(User user) {
        if (user.getId() < 0) {
            user.setId(System.nanoTime());
        }

        users.add(user);
    }

    /**
     * remove user
     *
     * @param user
     */
    public void remove(User user) {
        users.remove(user);
    }

    /**
     * load all user
     *
     * @return users
     */
    public Set<User> loadAll() {
        return users;
    }

}
