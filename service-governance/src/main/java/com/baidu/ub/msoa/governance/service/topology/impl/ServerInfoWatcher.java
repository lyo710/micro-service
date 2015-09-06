package com.baidu.ub.msoa.governance.service.topology.impl;

import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace.NameSpace;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/24.
 */
// @Component
public class ServerInfoWatcher implements Watcher {

    @PostConstruct
    public void init() {
        client.addWathcer(this);
    }

    @Resource
    private ZooKeeperClient client;

    @Resource
    private ServiceTopologySubscriberManager subscriberManager;

    @Override
    public void process(WatchedEvent event) {

        if (event.getPath() == null || !event.getPath().startsWith(NameSpace.SERVER_INFO_ROOT)) {
            return;
        }

        String[] paths = event.getPath().split("/");
        if (paths.length != 3) {
            return;
        }

        String host = paths[1];
        String provider = client.getData(event.getPath(), String.class);
        String serviceIdentity = paths[2];

        switch (event.getType()) {
            case NodeCreated:

            case NodeDeleted:

            default:

        }
    }
}
