package com.baidu.ub.msoa.governance.domain.repository;

import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;

/**
 * Created by pippo on 15/8/4.
 */
public interface ServiceInfoRepository {

    /**
     * 获取service info
     *
     * @param provider
     * @param serviceIdentity
     * @return ServiceInfo
     */
    ServiceInfo get(int provider, String serviceIdentity);

    /**
     * 获取service info
     *
     * @param provider
     * @param consumer
     * @param serviceIdentity
     * @return ServiceInfo
     */
    ServiceInfo get(int provider, String consumer, String serviceIdentity);

    /**
     * 保存service info
     *
     * @param serviceInfo
     */
    void save(ServiceInfo serviceInfo);

    /**
     * 更新service info
     *
     * @param serviceInfo
     */
    void update(ServiceInfo serviceInfo);
}
