package com.baidu.ub.msoa.container.support.router.conf;

/**
 * 系统内使用的所有事件驱动器名称
 *
 * @author zhangxu
 */
public interface EventEngineNames {

    /**
     * 服务端事件处理引擎
     */
    String PROCESS_EVENT_ENGINE = "event.process.engine";

    /**
     * 兼容<tt>navi 1.0</tt>的服务端事件处理引擎
     */
    String PROCESS_EVENT_ENGINE_FOR_LEGACY = "event.process.navi.legacy.engine";

    /**
     * 客户端事件处理引擎
     */
    String ROUTE_EVENT_ENGINE = "event.route.engine";

    /**
     * 兼容<tt>navi 1.0</tt>的客户端事件处理引擎
     */
    String ROUTE_EVENT_ENGINE_FOR_LEGACY = "event.route.navi.legacy.engine";

}
