package com.baidu.ub.msoa.governance.domain.repository.impl;

import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatistics;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatus;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ServiceTopologyRepository;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace.NameSpace;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/23.
 */
@Repository
public class ZKServiceTopologyRepository implements ServiceTopologyRepository {

    @Resource
    private ZooKeeperClient client;

    @Override
    public ServiceTopologyView getView(int provider, String service, int version, String method) {
        String serviceIdentity = BundleServiceNameSpace.NameSpace.serviceIdentity(provider, service, version, method);
        return getView(provider, serviceIdentity);
    }

    @Override
    public ServiceTopologyView getView(int provider, String serviceIdentity) {
        ServiceTopologyView view =
                client.getData(NameSpace.serviceTopology(provider, serviceIdentity), ServiceTopologyView.class);

        for (String host : client.getChildren(NameSpace.serviceEndpoints(provider, serviceIdentity))) {
            String[] hh = host.split("#");
            String ip = hh[0];
            int port = Integer.parseInt(hh[1]);

            Endpoint endpoint = new Endpoint(view.provider, view.service, view.version, view.method, ip, port);

            endpoint.setStatus(client.getData(NameSpace.serviceEndpointStatus(provider, serviceIdentity, ip, port),
                    EndpointStatus.class));

            endpoint.setStatistics(client.getData(NameSpace.serviceEndpointStatistics(provider,
                    serviceIdentity,
                    host,
                    port), EndpointStatistics.class));

            view.getEndpoints().add(endpoint);
        }

        return view;
    }

    @Override
    public List<Integer> getAllProvider() {
        List<Integer> ids = new ArrayList<>();
        List<String> idsstr = client.getChildren(NameSpace.SERVICE_TOPOLOGY_ROOT);
        for (String idstr : idsstr) {
            ids.add(Integer.parseInt(idstr));
        }
        return ids;
    }

    @Override
    public List<ServiceTopologyView> getAllServiceByProvider(int provider) {
        List<ServiceTopologyView> views = new ArrayList<>();

        List<String> serviceIdentities = client.getChildren(NameSpace.providerTopology(provider));
        for (String serviceIdentity : serviceIdentities) {
            views.add(getView(provider, serviceIdentity));
        }

        return views;
    }
}
