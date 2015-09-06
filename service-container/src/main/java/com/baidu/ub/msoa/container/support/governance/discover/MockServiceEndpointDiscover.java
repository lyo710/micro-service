package com.baidu.ub.msoa.container.support.governance.discover;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;

import java.util.Collection;

/**
 * Created by pippo on 15/6/25.
 */
// @Service
// @TestProfile
public class MockServiceEndpointDiscover implements ServiceEndpointDiscover {

    @Override
    public Endpoint select(BundleServiceMetaInfo metaInfo) {
        return null;
    }

    @Override
    public Endpoint select(BundleServiceMetaInfo metaInfo, SelectStrategy strategy) {
        return null;
    }

    @Override
    public Collection<Endpoint> list(BundleServiceMetaInfo metaInfo) {
        return null;
    }
}
