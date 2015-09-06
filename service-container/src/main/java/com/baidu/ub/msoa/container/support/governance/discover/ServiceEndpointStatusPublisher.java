package com.baidu.ub.msoa.container.support.governance.discover;

import asia.stampy.client.message.connect.ConnectMessage;
import com.baidu.ub.msoa.container.support.JPAASEnvironment;
import com.baidu.ub.msoa.container.support.governance.domain.dto.spring.ServiceRegisterAcceptEvent;
import com.baidu.ub.msoa.container.support.governance.domain.dto.stomp.EndpointActiveMessage;
import com.baidu.ub.msoa.container.support.governance.domain.model.registry.ServiceInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatus;
import com.baidu.ub.msoa.pubusb.websocket.SubscribeClientEndpoint.LifeCycleListener;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pippo on 15/8/30.
 */
@Service("serviceEndpointStatusPublisher")
public class ServiceEndpointStatusPublisher extends RemoteServiceEndpointDiscover
        implements ApplicationListener<ServiceRegisterAcceptEvent> {

    private static Map<String, Endpoint> activedEndpoints = new ConcurrentHashMap<>();

    @Resource(name = "jpaasEnvironment")
    private JPAASEnvironment environment;

    @PostConstruct
    @Override
    public void init() {
        clientEndpoint.setLifeCycleListener(new SubscriberReconnectListener());
        super.init();
    }

    @PreDestroy
    public void destroy() {
        super.close();
    }

    @Override
    public void onApplicationEvent(ServiceRegisterAcceptEvent event) {
        ServiceInfo info = (ServiceInfo) event.getSource();

        /* 记录住已经发送过的active endpoint,以便重连以后再次发送 */
        Endpoint endpoint = activedEndpoints.get(info.serviceIdentity);
        if (endpoint == null) {
            endpoint = new Endpoint(info.provider,
                    info.service,
                    info.version,
                    info.method,
                    environment.getHostIP(),
                    environment.getHostHttpPort());
            endpoint.setStatus(EndpointStatus.online);
            activedEndpoints.put(info.serviceIdentity, endpoint);
        }

        active(endpoint);
    }

    private void active(Endpoint endpoint) {
        sendMessage(new EndpointActiveMessage(endpoint));
    }

    private class SubscriberReconnectListener implements LifeCycleListener {

        @Override
        public void onOpen(Session session, EndpointConfig endpointConfig) {
            /* 发送当前容器bind的ip */
            session.getAsyncRemote()
                    .sendText(new ConnectMessage(environment.getHostIP()
                            + "#"
                            + environment.getHostHttpPort()).toStompMessage(true));

            /* 当重连成功后要发送可以激活的service endpoint */
            for (Endpoint endpoint : activedEndpoints.values()) {
                active(endpoint);
            }
        }

        @Override
        public void onClose(Session session) {

        }
    }
}
