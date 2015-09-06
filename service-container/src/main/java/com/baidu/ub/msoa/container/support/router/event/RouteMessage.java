package com.baidu.ub.msoa.container.support.router.event;

import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.util.Arrays;

/**
 * Created by pippo on 15/6/23.
 */
public class RouteMessage {

    public RouteMessage() {

    }

    public RouteMessage(int provider, String service, int version, String method, RouteStrategy routeStrategy) {
        meta = new RouteMeta(provider, service, version, method, routeStrategy);
    }

    public RouteMessage(String traceId,
            String sequence,
            String signature,
            int provider,
            String service,
            int version,
            String method,
            RouteStrategy routeStrategy) {
        meta = new RouteMeta(traceId, sequence, signature, provider, service, version, method, routeStrategy);
    }

    public RouteMeta meta;
    public Class<?>[] argumentTypes;
    public Object[] arguments;
    public Class<?> resultType;
    public Object result;

    public RouteMeta getMeta() {
        return meta;
    }

    public void setMeta(RouteMeta meta) {
        this.meta = meta;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format(
                "RouteMessage{'meta'=%s, 'argumentTypes'=%s, 'arguments'=%s, 'resultType'=%s, 'result'=%s}",
                meta,
                Arrays.toString(argumentTypes),
                Arrays.toString(arguments),
                resultType,
                result);
    }
}
