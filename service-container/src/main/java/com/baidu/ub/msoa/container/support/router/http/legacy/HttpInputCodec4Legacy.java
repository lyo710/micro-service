package com.baidu.ub.msoa.container.support.router.http.legacy;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.InputEvent;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandler;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.proto.ProtostuffReflect4Legacy;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

/**
 * <tt>navi 1.0</tt>服务端响应请求核心事件handler
 *
 * @author zhangxu
 */
@Component
public class HttpInputCodec4Legacy implements EventHandler<InputEvent>, RouterConstants {

    @Override
    public String getName() {
        return HttpInputCodec4Legacy.class.getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.HTTP_INPUT.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, InputEvent event) throws Throwable {
        RequestDTO requestDTO = ProtostuffReflect4Legacy.decode(RequestDTO.class, event.input);
        event.message.meta.serviceIdentity =
                BundleServiceNameSpace.NameSpace.serviceIdentity(event.message.meta.provider,
                        event.message.meta.service,
                        1,
                        requestDTO.getMethod());
        MethodExecutor executor = getLocalMethodExporter(event.message.meta.serviceIdentity);
        if (executor == null) {
            throw new ServiceMethodNotFoundException(String.format("can not find local method:[%s]", event.message));
        }
        event.message.arguments = requestDTO.getParameters();
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, InputEvent event) throws Throwable {
        if (event.getOutput() != null) {
            return;
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResult(event.message.result);
        responseDTO.setStatus(NaviLegacyConstants.StatusCode.RPC_OK);
        event.output = ProtostuffReflect4Legacy.encode(responseDTO);
    }

    @Override
    public void onException(EventHandlerContext context, InputEvent event, Throwable throwable) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus(NaviLegacyConstants.StatusCode.SYS_ERROR);
        responseDTO.setError(new RuntimeException(throwable.getMessage(), throwable));
        event.output = ProtostuffReflect4Legacy.encode(responseDTO);
    }

    /**
     * 此处为了做兼容老框架特别处理，老框架对于异常会利用<code>getCause</code>方法获取真正异常，因此需要嵌套异常。
     * 同时，新框架的异常体系老框架并不知晓，为防止序列化抛出<code>ClassNotFoundException</code>，
     * 统一使用<code>RuntimeException</code>
     * 如果<tt>e</tt>不为空，则默认客户端可以接受re-throw这个异常出去；否则包装<code>RuntimeException</code>，
     * 其错误消息单独由调用者提供。
     *
     * @param e       异常
     * @param message 异常消息
     * @return 运行时异常，用户客户端还原抛出使用
     */
    private static RuntimeException createException(Exception e, String message) {
        if (e != null) {
            return new RuntimeException(e);
        } else {
            return new RuntimeException(new RuntimeException(message));
        }
    }

    protected MethodExecutor getLocalMethodExporter(String name) {
        return BundleContainer.get().getBean(name);
    }

}
