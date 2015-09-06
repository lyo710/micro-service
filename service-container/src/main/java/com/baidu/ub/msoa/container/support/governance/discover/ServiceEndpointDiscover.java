package com.baidu.ub.msoa.container.support.governance.discover;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;

import java.util.Collection;

/**
 * Created by pippo on 15/6/25.
 */
public interface ServiceEndpointDiscover {

    /**
     * select available service endpoint with default strategy
     *
     * @param metaInfo
     * @return ServiceRegisterEntry
     */
    Endpoint select(BundleServiceMetaInfo metaInfo);

    /**
     * select available service endpoint with given strategy
     *
     * @param metaInfo
     * @return ServiceRegisterEntry
     */
    Endpoint select(BundleServiceMetaInfo metaInfo, SelectStrategy strategy);

    /**
     * @param metaInfo
     * @return all ServiceRegisterEntry
     */
    Collection<Endpoint> list(BundleServiceMetaInfo metaInfo);

    enum SelectStrategy {

        random,

        roundrobin,

        connectionMinimum,

        latencyMinimum
    }
}
