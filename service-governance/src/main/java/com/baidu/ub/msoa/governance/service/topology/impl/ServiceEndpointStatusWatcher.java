package com.baidu.ub.msoa.governance.service.topology.impl;

import com.baidu.ub.msoa.container.support.governance.domain.dto.stomp.TopologyViewMessage;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ServiceTopologyRepository;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pippo on 15/8/24.
 */
@Component
public class ServiceEndpointStatusWatcher implements Watcher, ZKPathNameSpace {

    private static Pattern pathPattern = Pattern.compile(NameSpace.serviceEndpointStatus("(.+)", "(.+)", "(.+)#(.+)"));

    @PostConstruct
    public void init() {
        client.addWathcer(this);
    }

    @Resource
    private ZooKeeperClient client;

    @Resource
    private ServiceTopologyRepository serviceTopologyRepository;

    @Resource
    private ServiceTopologySubscriberManager subscriberManager;

    @Override
    public void process(WatchedEvent event) {

        if (event.getPath() == null) {
            return;
        }

        Matcher matcher = pathPattern.matcher(event.getPath());
        if (!matcher.matches()) {
            return;
        }

        int provider = Integer.parseInt(matcher.group(1));
        String serviceIdentity = matcher.group(2);

        ServiceTopologyView view = serviceTopologyRepository.getView(provider, serviceIdentity);
        subscriberManager.publish(view.serviceIdentity, new TopologyViewMessage(view));
    }
}
