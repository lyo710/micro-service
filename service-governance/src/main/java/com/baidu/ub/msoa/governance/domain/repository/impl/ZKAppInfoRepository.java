package com.baidu.ub.msoa.governance.domain.repository.impl;

import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.model.AppInfo;
import com.baidu.ub.msoa.governance.domain.repository.AppInfoRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/31.
 */
@Repository
public class ZKAppInfoRepository implements AppInfoRepository {

    private static String appInfoRoot = "/app_info";

    @Resource
    private ZooKeeperClient client;

    @Override
    public AppInfo get(int id) {
        return client.getData(getAppInfoPath(id), AppInfo.class);
    }

    @Override
    public void persist(AppInfo appInfo) {
        client.setData(getAppInfoPath(appInfo.getId()), appInfo);
    }

    @Override
    public List<AppInfo> loadAll() {
        List<AppInfo> infos = new ArrayList<>();

        List<String> ids = client.getChildren(appInfoRoot);
        for (String id : ids) {
            infos.add(get(Integer.parseInt(id)));
        }

        return infos;
    }

    @Override
    public void delete(int appId) {
        client.delete(getAppInfoPath(appId));
    }

    private String getAppInfoPath(int id) {
        return String.format("%s/%s", appInfoRoot, id);
    }
}
