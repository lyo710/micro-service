package com.baidu.ub.msoa.governance.domain.repository;

import com.baidu.ub.msoa.governance.domain.model.AppInfo;

import java.util.List;

/**
 * Created by pippo on 15/8/31.
 */
public interface AppInfoRepository {

    AppInfo get(int id);

    void persist(AppInfo appInfo);

    List<AppInfo> loadAll();

    void delete(int appId);
}
