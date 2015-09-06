package com.baidu.ub.msoa.container.support.router.http;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RouteStatus;
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
 * Created by pippo on 15/7/19.
 */
@Component
public class HttpRouter extends EventHandlerAdapter<RouteEvent> implements RouterConstants {

    private static String HTTP_URL_FORMAT = "http://%s:%s" + RPC_ENDPOINT;

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.HTTP_ROUTE.ordinal();
    }

    @Override
    public void downstream(final EventHandlerContext context, final RouteEvent event) throws Throwable {
        final Endpoint destination = event.endpoint;
        final String url = String.format(HTTP_URL_FORMAT, destination.getHost(), destination.getPort());
        HttpPost post = new HttpPost(url);
        post.setConfig(CONFIG);
        post.setHeader(RPC_TRACE_ID, (String) context.getAttribute(RPC_TRACE_ID));
        post.setHeader(RPC_SEQUENCE, (String) context.getAttribute(RPC_SEQUENCE));
        post.setHeader(RPC_SIGNATURE, (String) context.getAttribute(RPC_SIGNATURE));
        post.setHeader(RPC_PROVIDER, context.getAttribute(RPC_PROVIDER) + "");
        post.setHeader(RPC_SERVICE, (String) context.getAttribute(RPC_SERVICE));
        post.setHeader(RPC_VERSION, context.getAttribute(RPC_VERSION) + "");
        post.setHeader(RPC_METHOD, (String) context.getAttribute(RPC_METHOD));
        post.setHeader(RPC_STATUS, RouteStatus.POST + "");
        post.setEntity(new ByteArrayEntity((byte[]) context.getAttribute(RPC_BODY)));

        HttpHelper.get().executeRequest(post, new ResponseHandler<Void>() {

            @Override
            public Void handleResponse(HttpResponse response) throws IOException {

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RouterException(String.format("can not route message to:[%s], the http status is:[%s]",
                            url,
                            response.getStatusLine().getStatusCode()));
                }

                if (Integer.valueOf(response.getFirstHeader(RPC_STATUS).getValue()) == RouteStatus.SUCCESS.code) {
                    context.setAttribute(RPC_BODY, ByteStreams.toByteArray(response.getEntity().getContent()));
                } else {
                    throw new RouterException(String.format("remote endpoint process due to error:[%s]",
                            response.getStatusLine().getStatusCode()));
                }

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
