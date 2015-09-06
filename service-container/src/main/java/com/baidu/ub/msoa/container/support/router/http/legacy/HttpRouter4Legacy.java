package com.baidu.ub.msoa.container.support.router.http.legacy;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.RouterException;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.http.HttpHelper;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <tt>navi 1.0</tt>客户端发起请求事件handler
 *
 * @author zhangxu
 */
@Component
public class HttpRouter4Legacy extends EventHandlerAdapter<RouteEvent> implements RouterConstants {

    /**
     * 远程服务url
     */
    private static final String HTTP_URL_FORMAT = "http://%s:%s" + RPC_ENDPOINT_FOR_NAVI10 + "/%s";

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.ROUTE.ordinal();
    }

    @Override
    public void downstream(final EventHandlerContext context, final RouteEvent event) throws Throwable {
        final Endpoint destination = event.getEndpoint();
        final String url =
                String.format(HTTP_URL_FORMAT, destination.getHost(), destination.getPort(), destination.getService());
        HttpPost post = new HttpPost(url);
        post.setConfig(CONFIG);
        post.setHeader(RPC_CONTENT_TYPE, (String) context.getAttribute(RPC_CONTENT_TYPE));
        post.setEntity(new ByteArrayEntity((byte[]) context.getAttribute(RPC_BODY)));

        HttpHelper.get().executeRequest(post, new ResponseHandler<Void>() {

            @Override
            public Void handleResponse(HttpResponse response) throws IOException {

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RouterException(String.format("can not route message to:[%s], the http status is:[%s]",
                            url,
                            response.getStatusLine().getStatusCode()));
                }

                context.setAttribute(RPC_BODY, ByteStreams.toByteArray(response.getEntity().getContent()));
                return null;
            }
        });
    }

    static RequestConfig CONFIG = RequestConfig.custom()
            .setConnectTimeout(1000 * 5)
            .setSocketTimeout(ROUTE_TIMEOUT)
            .setConnectionRequestTimeout(ROUTE_TIMEOUT)
            .build();

}
