package com.baidu.ub.msoa.container.support.rpc.inbound;

import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.baidu.ub.msoa.container.support.rpc.RESTConstants;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCDestination;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCHeader;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.container.support.rpc.domain.model.EventType;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pippo on 15/6/25.
 */
@WebServlet(asyncSupported = true, urlPatterns = { RESTConstants.REST_ENDPOINT_PATH })
public class HttpRESTInbound extends HttpInbound implements RESTConstants {

    private static Logger logger = LoggerFactory.getLogger(HttpRESTInbound.class);

    /* /navi2/api/{provider}/{service}/{version}/{method} */
    private static Pattern restFormat = Pattern.compile("/navi2/rest/(\\d+)/(.+)/(\\d+)/(.+)");

    @Override
    protected RPCEvent buildEvent(HttpServletRequest req) throws IOException {

        Matcher matcher = restFormat.matcher(req.getRequestURI());

        if (!matcher.matches()) {
            throw new InvalidRestURIException(String.format(
                    "request uri:[%s] not match /navi2/api/{provider}/{service}/{version}/{method}",
                    req.getRequestURI()));
        }

        RPCRequest request = new RPCRequest();
        request.id = getRequestId(req);
        request.header = new RPCHeader();
        request.header.destination = new RPCDestination(Integer.parseInt(matcher.group(1)),
                matcher.group(2),
                Integer.parseInt(matcher.group(3)),
                matcher.group(4),
                RouteStrategy.local);

        request.header.trace.traceId = getTraceId(req);
        request.arguments = new RPCArguments();
        request.arguments.codecType = codecType(req).code;
        request.arguments.payload = ByteStreams.toByteArray(req.getInputStream());

        RPCEvent event = new RPCEvent(EventType.inbound);
        event.payload.request = request;

        return event;
    }

    @Override
    protected void writeResponse(HttpServletResponse response, CodecType codecType, RPCResponse rpcResponse) {
        try {
            response.setContentType(codecType.type);
            response.setStatus(rpcResponse.status);
            response.getOutputStream().write(rpcResponse.result.payload);
        } catch (IOException e) {
            logger.error("write back message due to error", e);
        }
    }
}
