package com.baidu.ub.msoa.container.support.governance.domain.model.topology;

import com.baidu.ub.msoa.container.support.governance.discover.ServiceEndpointDiscover.SelectStrategy;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint.EndpointComparator;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by pippo on 15/8/3.
 */
public class ServiceTopologyView extends BundleServiceMetaInfo {

    private transient Endpoint[] sorted;

    public ServiceTopologyView() {

    }

    public ServiceTopologyView(int provider, String service, int version, String method) {
        super(provider, service, version, method, RouteStrategy.switchover);
    }

    public Endpoint select(SelectStrategy strategy) {
        //        if (!available) {
        //            return null;
        //        }

        if (sorted == null) {
            sortEndpoint();
        }

        if (sorted.length == 0) {
            return null;
        }

        switch (strategy) {
            case roundrobin:
                // TODO
                return sorted[0];
            case latencyMinimum:
                // TODO
                return sorted[0];
            default:
                return sorted[new Random().nextInt(sorted.length)];
        }
    }

    private void sortEndpoint() {
        TreeSet<Endpoint> toSort = new TreeSet<>(new EndpointComparator());
        for (Endpoint endpoint : endpoints) {
            if (endpoint.getStatus() == EndpointStatus.online) {
                toSort.add(endpoint);
            }
        }
        sorted = toSort.toArray(new Endpoint[toSort.size()]);
    }

    // 服务是否可用
    private boolean available = true;

    // 使用该服务的应用
    private Set<String> depended = new HashSet<>();

    // 提供能力的服务节点
    private Set<Endpoint> endpoints = new HashSet<>();

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Set<String> getDepended() {
        return depended;
    }

    public void setDepended(Set<String> depended) {
        this.depended = depended;
    }

    @SuppressWarnings("unchecked")
    public Set<Endpoint> getEndpoints() {
        return (Set<Endpoint>) (endpoints != null ? endpoints : Collections.emptySet());
    }

    public void setEndpoints(Set<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

}
