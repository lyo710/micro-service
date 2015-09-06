package com.baidu.ub.msoa.container.support.router;

/**
 * Created by pippo on 15/6/24.
 */
public enum RouteStrategy {

    /**
     * 只查找进程内服务并调用
     */
    local,

    /**
     * 只查找远程服务并调用
     */
    remote,

    /**
     * 可切换(tps低时优先本地,tps高时优先远程)
     */
    switchover,
}
