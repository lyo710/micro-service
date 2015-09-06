package com.baidu.ub.msoa.governance.domain.repository.impl;

import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;
import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ServiceInfoRepository;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/10.
 */
@Repository
public class ZKServiceInfoRepository implements ServiceInfoRepository, ZKPathNameSpace {

    @Resource
    private ZooKeeperClient client;

    @Override
    public ServiceInfo get(int provider, String serviceIdentity) {
        String path = NameSpace.serviceInfo(provider, serviceIdentity);
        return client.getData(path, ServiceInfo.class);
    }

    @Override
    public ServiceInfo get(int provider, String consumer, String serviceIdentity) {
        ServiceInfo info = get(provider, consumer);
        if (info == null) {
            return null;
        }

        /* 如果为空,那么认为接受所有consumer */
        if (info.getAcceptConsumers().isEmpty()) {
            return info;
        }

        if (info.getAcceptConsumers().contains(consumer)) {
            return info;
        }

        return null;
    }

    @Override
    public void save(ServiceInfo serviceInfo) {
        String path = NameSpace.serviceInfo(serviceInfo.provider, serviceInfo.serviceIdentity);
        client.create(path, serviceInfo);
    }

    @Override
    public void update(ServiceInfo serviceInfo) {
        String path = NameSpace.serviceInfo(serviceInfo.provider, serviceInfo.serviceIdentity);
        client.setData(path, serviceInfo);
    }

}
