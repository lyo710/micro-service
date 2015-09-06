package com.baidu.ub.msoa.governance.inbound.websocket;

import asia.stampy.client.message.connect.ConnectMessage;
import asia.stampy.client.message.subscribe.SubscribeMessage;
import asia.stampy.client.message.unsubscribe.UnsubscribeMessage;
import asia.stampy.common.message.StampyMessage;
import asia.stampy.common.parsing.StompMessageParser;
import asia.stampy.common.parsing.UnparseableException;
import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatus;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventEngine;
import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import com.baidu.ub.msoa.pubusb.event.MessageEvent;
import com.baidu.ub.msoa.pubusb.event.SubscribeEvent;
import com.baidu.ub.msoa.pubusb.event.UnSubscribeEvent;
import com.baidu.ub.msoa.pubusb.websocket.SubscribeServerEndpoint;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by pippo on 15/8/21.
 */
@ServerEndpoint(ServiceTopologySubscribeEndpoint.PATH)
public class ServiceTopologySubscribeEndpoint extends SubscribeServerEndpoint implements ZKPathNameSpace {

    public static final String PATH = "/service-governance/topology/subscribe";
    private static Logger logger = LoggerFactory.getLogger(ServiceTopologySubscribeEndpoint.class);
    private static StompMessageParser parser = new StompMessageParser();

    public ServiceTopologySubscribeEndpoint() {

    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        String sessionId = session.getId();
        Event<?> event = null;

        try {
            StampyMessage message = parser.parseMessage(msg);
            logger.debug("receive message:[{}]", message.getHeader());

            switch (message.getMessageType()) {
                case CONNECT:
                    endpointConnect(session, (ConnectMessage) message);
                    break;
                case SUBSCRIBE:
                    event = new SubscribeEvent(sessionId, (SubscribeMessage) message);
                    break;
                case UNSUBSCRIBE:
                    event = new UnSubscribeEvent(sessionId, (UnsubscribeMessage) message);
                    break;
                case MESSAGE:
                    event = new MessageEvent(sessionId, (MessageMessage) message);
                    break;
                default:
                    logger.debug("ignore message:[{}]", message.getHeader());
                    break;
            }
        } catch (UnparseableException e) {
            invalidMessage(msg, session);
        }

        if (event != null) {
            getEventEngine().process(event);
        }
    }

    @Override
    public void onClose(Session session) {
        endpointDisconnect(session);
        super.onClose(session);
    }

    private void endpointConnect(Session session, ConnectMessage message) {
        String host = message.getHeader().getHost();
        session.getUserProperties().put(CLIENT_ADDRESS, host);
        getZooKeeper().create(NameSpace.serverInfo(host), null);
    }

    private void endpointDisconnect(Session session) {
        String host = (String) session.getUserProperties().get(CLIENT_ADDRESS);
        if (Strings.isNullOrEmpty(host)) {
            return;
        }

        String path = NameSpace.serverInfo(host);

        try {
            for (String serviceIdentity : getZooKeeper().getChildren(path)) {
                String provider = getZooKeeper().getData(path + "/" + serviceIdentity, String.class);
                getZooKeeper().setData(NameSpace.serviceEndpointStatus(provider, serviceIdentity, host),
                        EndpointStatus.offline);
            }

            logger.debug("delete zk path:[{}]", path);
            getZooKeeper().delete(path);
        } catch (Exception e) {
            logger.warn(String.format("delete zk path:[%s] deu to error", path), e);
        }
    }

    private void invalidMessage(String msg, Session session) {

    }

    private SubscriberManager subscriberManager;
    private EventEngine eventEngine;
    private ZooKeeperClient client;

    @Override
    protected SubscriberManager getSubscriberManager() {
        if (subscriberManager == null) {
            subscriberManager = BundleContainer.get().getBean("serviceTopologySubscriberManager");
        }

        return subscriberManager;
    }

    protected EventEngine getEventEngine() {
        if (eventEngine == null) {
            eventEngine = BundleContainer.get().getBean("serviceTopologySubscribeEventEngine");
        }

        return eventEngine;
    }

    protected ZooKeeperClient getZooKeeper() {
        if (client == null) {
            client = BundleContainer.get().getBean("zooKeeperClient");
        }

        return client;
    }
}
