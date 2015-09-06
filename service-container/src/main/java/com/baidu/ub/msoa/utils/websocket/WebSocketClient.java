package com.baidu.ub.msoa.utils.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * Created by pippo on 15/9/5.
 */
public class WebSocketClient {

    /* client container是可以公用的 */
    private static WebSocketContainer container = null;

    public static WebSocketContainer getContainer() {
        if (container == null) {
            container = ContainerProvider.getWebSocketContainer();
            container.setAsyncSendTimeout(1000 * 60 * 10);

        }

        return container;
    }

    public static Session connect(Class<?> annotatedEndpointClass, URI path) throws IOException, DeploymentException {
        return getContainer().connectToServer(annotatedEndpointClass, path);
    }

    public static Session connect(Object annotatedEndpointInstance, URI path) throws IOException, DeploymentException {
        return getContainer().connectToServer(annotatedEndpointInstance, path);
    }
}
