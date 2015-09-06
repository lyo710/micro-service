package com.baidu.ub.msoa.container.support.router.conf;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.RouteMeta;
import com.baidu.ub.msoa.event.EventEngine;

/**
 * 容器内所有使用的{@link EventEngine}工厂
 *
 * @author zhangxu
 */
public class EventEngineFactory {

    /**
     * {@link RouteMeta#getProvider()}如何符合这个pattern，则认为某个客户端是调用navi 1.0服务的
     */
    public static final String PROVIDER_PREFIX_NAVI10 = "navi1.0/";

    private static EventEngine PROCESS_ENGINE;

    private static EventEngine PROCESS_ENGINE_FOR_LEGACY;

    private static EventEngine ROUTE_ENGINE;

    private static EventEngine ROUTE_ENGINE_FOR_LEGACY;

    static {
        PROCESS_ENGINE = BundleContainer.get().getBean(EventEngineNames.PROCESS_EVENT_ENGINE);
        PROCESS_ENGINE_FOR_LEGACY = BundleContainer.get().getBean(EventEngineNames.PROCESS_EVENT_ENGINE_FOR_LEGACY);
        ROUTE_ENGINE = BundleContainer.get().getBean(EventEngineNames.ROUTE_EVENT_ENGINE);
        ROUTE_ENGINE_FOR_LEGACY = BundleContainer.get().getBean(EventEngineNames.ROUTE_EVENT_ENGINE_FOR_LEGACY);
    }

    public static EventEngine getProcessEngineByEndPointUrl(String endPointUrl) {
        if (endPointUrl.contains(RouterConstants.RPC_ENDPOINT_FOR_NAVI10)) {
            return PROCESS_ENGINE_FOR_LEGACY;
        } else {
            return PROCESS_ENGINE;
        }
    }

    public static EventEngine getRouteEngineByProvider(int provider) {
        if (provider == 9999) {
            return ROUTE_ENGINE_FOR_LEGACY;
        } else {
            return ROUTE_ENGINE;
        }
    }

}
