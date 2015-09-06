package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCDestination extends BundleServiceMetaInfo {

    public RPCDestination() {

    }

    public RPCDestination(int provider, String service, int version, String method, RouteStrategy routeStrategy) {
        super(provider, service, version, method, routeStrategy);
    }

}
