package com.baidu.ub.msoa.container.support.router;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;

/**
 * Created by pippo on 15/7/20.
 */
public interface RPCClient {

    <T> T send(Endpoint destination, Class<T> returnType, Object... parameters);

}
