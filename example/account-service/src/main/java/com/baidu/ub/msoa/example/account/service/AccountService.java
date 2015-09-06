package com.baidu.ub.msoa.example.account.service;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.example.account.domain.model.Account;

/**
 * Created by pippo on 15/8/20.
 */
@BundleService(name = "accountService", version = 1)
public interface AccountService {

    /**
     * account login
     *
     * @param name
     * @param password
     * @return account
     */
    Account login(String name, String password);

}
