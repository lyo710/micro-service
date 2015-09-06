package com.baidu.ub.msoa.container.support.router.method;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.RPCEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.proto.ProtostuffReflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class LocalMethodExecutor extends EventHandlerAdapter<RPCEvent> implements RouterConstants {

    private static Logger logger = LoggerFactory.getLogger(LocalMethodExecutor.class);

    @Override
    public boolean accept(Event<?> event) {
        return event instanceof RPCEvent;
    }

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        MethodExecutor executor = getLocalMethodExporter(event.message.meta.serviceIdentity);

        if (executor == null) {
            switch (event.message.meta.routeStrategy) {
                case local:
                    throw new ServiceMethodNotFoundException(String.format("can not find local:[%s]", event.message));
                default:
                    context.passThrough(event);
                    return;
            }
        }

        event.message.result = localInvoke(event.message, executor);
    }

    @Override
    public void upstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        if (event.message.result == null || event.message.resultType == null) {
            return;
        }

        /* 如果调用的本地方法的返回类型和需要类型不符,尝试进行转换 */
        if (event.message.result.getClass() != event.message.resultType && !event.message.resultType.isAssignableFrom(
                event.message.result.getClass())) {
            event.message.result = transform(event.message.resultType, event.message.result);
        }

    }

    protected MethodExecutor getLocalMethodExporter(String name) {
        return BundleContainer.get().getBean(name);
    }

    protected Object localInvoke(RouteMessage message, MethodExecutor executor) {

        if (logger.isTraceEnabled()) {
            logger.trace("invoke executor:[{}]", message.meta.serviceIdentity);
        }

        return executor.execute(message.arguments);
    }

    protected <T> T transform(Class<T> expectType, Object origin) {
        return ProtostuffReflect.transform(expectType, origin);
    }
}
