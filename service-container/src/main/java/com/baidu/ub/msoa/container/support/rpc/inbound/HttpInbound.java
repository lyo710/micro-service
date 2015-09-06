package com.baidu.ub.msoa.container.support.rpc.inbound;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.rpc.HttpConstants;
import com.baidu.ub.msoa.container.support.rpc.RPCStatus;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.ErrorInfo;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResult;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.container.support.rpc.inbound.event.InboundEventEngine;
import com.baidu.ub.msoa.event.EventCallback;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pippo on 15/9/2.
 */
public abstract class HttpInbound extends HttpServlet implements HttpConstants {

    private static Logger logger = LoggerFactory.getLogger(HttpInbound.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RPCEvent event;

        try {
            event = buildEvent(req);
        } catch (Throwable throwable) {
            logger.warn("build event due to error", throwable);
            writeResponse(resp, codecType(req), errorResponse(req, throwable));
            return;
        }

        if (logger.isTraceEnabled()) {
            logger.trace("accept inbound event:[{}]", event);
        }

        engine().process(event, new RPCEventCallback(req, resp, req.startAsync()));
    }

    protected abstract RPCEvent buildEvent(HttpServletRequest req) throws IOException;

    protected int getRequestId(HttpServletRequest req) {
        return req.getIntHeader(NAVI2_RPC_REQUEST_ID_NAME);
    }

    protected String getTraceId(HttpServletRequest req) {
        return req.getHeader(NAVI2_RPC_TRACE_ID_NAME);
    }

    protected CodecType codecType(HttpServletRequest req) {
        return CodecType.from(req.getContentType());
    }

    protected RPCResponse errorResponse(HttpServletRequest req, Throwable throwable) {
        RPCResult result = new RPCResult(codecType(req), new ErrorInfo(throwable));
        return new RPCResponse(getRequestId(req), RPCStatus.INNER_ERROR.code, result);
    }

    protected void writeResponse(HttpServletResponse response, CodecType codecType, RPCResponse rpcResponse) {
        try {
            response.setStatus(rpcResponse.status);
            response.getOutputStream().write(CodecFactory.getCodec(codecType).encode(response));
        } catch (IOException e) {
            logger.error("write back message due to error", e);
        }
    }

    protected InboundEventEngine engine;

    public InboundEventEngine engine() {
        if (engine == null) {
            engine = BundleContainer.get().getBean("rpc.inboundEventEngine");
        }

        return engine;
    }

    protected class RPCEventCallback implements EventCallback<RPCEvent> {

        public RPCEventCallback(HttpServletRequest req, HttpServletResponse resp, AsyncContext context) {
            this.req = req;
            this.resp = resp;
            this.context = context;
        }

        HttpServletRequest req;
        HttpServletResponse resp;
        AsyncContext context;

        @Override
        public void preInvoke(RPCEvent event) {

        }

        @Override
        public void onSuccess(RPCEvent event) {
            if (logger.isTraceEnabled()) {
                logger.trace("write back message:[{}]", event);
            }

            RPCResponse rpcResponse = event.payload.response;
            rpcResponse.status = RPCStatus.SUCCESS.code;
            writeAsyncResponse(context, codecType(req), rpcResponse);
        }

        @Override
        public void onFail(RPCEvent event, Throwable throwable) {
            String stackTrace = Throwables.getStackTraceAsString(throwable);
            logger.warn("process event:[{}] due to error:[{}]", event, stackTrace);
            writeAsyncResponse(context, codecType(req), errorResponse(req, throwable));
        }

        protected void writeAsyncResponse(AsyncContext context, CodecType codecType, RPCResponse response) {
            try {
                writeResponse((HttpServletResponse) context.getResponse(), codecType, response);
            } finally {
                context.complete();
            }
        }

    }

}
