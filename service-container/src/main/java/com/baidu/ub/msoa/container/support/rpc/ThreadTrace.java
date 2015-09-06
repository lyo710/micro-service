package com.baidu.ub.msoa.container.support.rpc;

import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCTrace;

/**
 * Created by pippo on 15/9/2.
 */
public class ThreadTrace {

    private static ThreadLocal<RPCTrace> threadTrace = new ThreadLocal<>();

    public static RPCTrace get() {
        return threadTrace.get();
    }

    public static void set(RPCTrace trace) {
        threadTrace.set(trace);
    }

}
