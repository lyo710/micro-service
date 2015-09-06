package com.baidu.ub.msoa.container.support.rpc.inbound;

import com.baidu.ub.msoa.container.support.rpc.RPCConstants;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by pippo on 15/6/25.
 */
@WebServlet(asyncSupported = true, urlPatterns = { RPCConstants.RPC_ENDPOINT })
public class HttpRPCInbound extends HttpInbound {

    @Override
    protected RPCEvent buildEvent(HttpServletRequest req) throws IOException {
        //        RPCEvent event = new RPCEvent(EventType.inbound);
        //        event.payload.request = new RPCRequest().decode(CodecFactory.getCodec(codecType(req)),
        //                ByteStreams.toByteArray(req.getInputStream()));
        //        return event;
        throw new RuntimeException("not impl yet");
    }

}
