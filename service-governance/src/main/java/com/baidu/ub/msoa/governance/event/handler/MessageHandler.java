package com.baidu.ub.msoa.governance.event.handler;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.container.support.governance.domain.dto.stomp.BodyCodec;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatus;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.governance.cluster.zk.ZooKeeperClient;
import com.baidu.ub.msoa.governance.domain.repository.ZKPathNameSpace;
import com.baidu.ub.msoa.pubusb.event.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/23.
 */
@Component
public class MessageHandler extends EventHandlerAdapter<MessageEvent> implements ZKPathNameSpace {

    private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Resource
    private ZooKeeperClient client;

    @Override
    public boolean accept(Event<?> event) {
        return event instanceof MessageEvent;
    }

    @Override
    public void downstream(EventHandlerContext context, MessageEvent event) throws Throwable {
        MessageMessage message = event.getPayload();
        switch (message.getHeader().getDestination()) {
            case "endpoint":
                processEndpointMessage(message);
                break;
            default:
                logger.warn("can not process message:[{}]", message.getHeader());
        }
    }

    private void processEndpointMessage(MessageMessage message) {
        EndpointStatus status = EndpointStatus.valueOf(message.getHeader().getSubscription());
        Endpoint endpoint = BodyCodec.decodeBase64((String) message.getBody(), Endpoint.class);

        switch (status) {
            case online:
                online(endpoint);
                break;
            case offline:
                offline(endpoint);
                break;
            default:
                logger.warn("endpoint status is not support now:[{}]", status);
        }
    }

    private void online(Endpoint endpoint) {
        client.create(NameSpace.serviceTopology(endpoint.provider, endpoint.serviceIdentity),
                new ServiceTopologyView(endpoint.provider, endpoint.service, endpoint.version, endpoint.method));

        client.setData(NameSpace.serviceEndpointStatus(endpoint.provider,
                endpoint.serviceIdentity,
                endpoint.getHost(),
                endpoint.getPort()), EndpointStatus.online);

        client.setData(getServerServiceIdentityPath(endpoint), endpoint.provider);
    }

    private void offline(Endpoint endpoint) {
        client.setData(NameSpace.serviceEndpointStatus(endpoint.provider,
                endpoint.serviceIdentity,
                endpoint.getHost(),
                endpoint.getPort()), EndpointStatus.offline);

        client.delete(getServerServiceIdentityPath(endpoint));
    }

    private String getServerServiceIdentityPath(Endpoint endpoint) {
        return NameSpace.serverInfo(endpoint.getHost(), endpoint.getPort()) + "/" + endpoint.serviceIdentity;
    }

}
