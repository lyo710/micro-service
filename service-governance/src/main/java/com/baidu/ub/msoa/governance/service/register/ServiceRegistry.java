package com.baidu.ub.msoa.governance.service.register;

import com.baidu.ub.msoa.container.support.governance.domain.model.registry.RegisterInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;

/**
 * Created by pippo on 15/8/5.
 */
public interface ServiceRegistry {

    /**
     * save schema
     *
     * @param provider
     * @param service
     * @param version
     * @param schema
     */
    void saveSchema(int provider, String service, int version, String schema);

    /**
     * @param provider
     * @param service
     * @param version
     * @return schema
     */
    String getSchema(int provider, String service, int version);

    /**
     * 相应服务信息注册请求,一个服务注册信息里面可能会包括多个服务信息
     *
     * @param infos
     */
    RegisterInfo[] register(ServiceInfo... infos) throws Exception;

}
