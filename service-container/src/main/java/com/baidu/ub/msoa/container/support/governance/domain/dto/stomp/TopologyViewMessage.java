package com.baidu.ub.msoa.container.support.governance.domain.dto.stomp;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.ServiceTopologyView;

import java.util.UUID;

/**
 * Created by pippo on 15/8/4.
 */
public class TopologyViewMessage extends MessageMessage {

    public TopologyViewMessage() {
        super("topology", UUID.randomUUID().toString(), "change");
    }

    public TopologyViewMessage(ServiceTopologyView view) {
        this();
        setServiceTopologyView(view);
    }

    public void setServiceTopologyView(ServiceTopologyView view) {
        setBody(BodyCodec.encodeBase64(view));
    }

    public ServiceTopologyView getServiceTopologyView() {
        return BodyCodec.decodeBase64((String) getBody(), ServiceTopologyView.class);
    }

}
