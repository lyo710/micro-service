package com.baidu.ub.msoa.container.support.router.http;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.InputEvent;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandler;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class HttpInputCodec extends ProtobufCodec implements EventHandler<InputEvent>, RouterConstants {

    @Override
    public String getName() {
        return HttpInputCodec.class.getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.HTTP_INPUT.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, InputEvent event) throws Throwable {
        MethodExecutor executor = getLocalMethodExporter(event.message.meta.serviceIdentity);
        if (executor == null) {
            throw new ServiceMethodNotFoundException(String.format("can not find local method:[%s]", event.message));
        }

        event.message.arguments = decode(event.input, executor.getParameterTypes());
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, InputEvent event) throws Throwable {
        event.output = encode(event.message.result);
    }

    @Override
    public void onException(EventHandlerContext context, InputEvent event, Throwable throwable) {

    }

    protected MethodExecutor getLocalMethodExporter(String name) {
        return BundleContainer.get().getBean(name);
    }
}
