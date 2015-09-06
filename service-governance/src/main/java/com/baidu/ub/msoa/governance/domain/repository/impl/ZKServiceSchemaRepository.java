package com.baidu.ub.msoa.governance.domain.repository.impl;

import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ServiceSchemaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/27.
 */
@Repository
public class ZKServiceSchemaRepository implements ServiceSchemaRepository {

    @Resource
    private ZooKeeperClient client;

    @Override
    public void save(int provider, String service, int version, String schema) {
        client.setData(String.format("/service_schema/%s/%s/%s", provider, service, version), schema);
    }

    @Override
    public String get(int provider, String service, int version) {
        return client.getData(String.format("/service_schema/%s/%s/%s", provider, service, version), String.class);
    }
}
