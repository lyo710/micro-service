package com.baidu.ub.msoa.governance.domain.repository;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;

import java.util.List;

/**
 * Created by pippo on 15/8/23.
 */
public interface ServiceTopologyRepository {

    /**
     * @param provider
     * @param serviceIdentity
     * @return ServiceTopologyView
     */
    ServiceTopologyView getView(int provider, String serviceIdentity);

    /**
     * @param provider
     * @param service
     * @param version
     * @param method
     * @return ServiceTopologyView
     */
    ServiceTopologyView getView(int provider, String service, int version, String method);

    List<Integer> getAllProvider();

    List<ServiceTopologyView> getAllServiceByProvider(int provider);
}
