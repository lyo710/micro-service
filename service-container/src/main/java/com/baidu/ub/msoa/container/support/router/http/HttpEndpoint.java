package com.baidu.ub.msoa.container.support.router.http;

import com.baidu.ub.msoa.container.support.router.RouteStatus;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.conf.EventEngineFactory;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.InputEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.EventCallback;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pippo on 15/6/25.
 */
@WebServlet(asyncSupported = true,
        urlPatterns = { RouterConstants.RPC_ENDPOINT, RouterConstants.RPC_ENDPOINT_FOR_NAVI10_URL_PATTERNS })
public class HttpEndpoint extends HttpServlet implements RouterConstants {

    private static Logger logger = LoggerFactory.getLogger(HttpEndpoint.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        // TODO 输入校验

        RouteMessage message = new RouteMessage(req.getHeader(RPC_TRACE_ID),
                req.getHeader(RPC_SEQUENCE),
                req.getHeader(RPC_SIGNATURE),
                Integer.parseInt(req.getHeader(RPC_PROVIDER)),
                req.getHeader(RPC_SERVICE),
                Integer.parseInt(req.getHeader(RPC_VERSION)),
                req.getHeader(RPC_METHOD),
                RouteStrategy.local);
        byte[] input = ByteStreams.toByteArray(req.getInputStream());
        InputEvent event = new InputEvent(EventType.HTTP_INPUT, message, input);

        if (logger.isTraceEnabled()) {
            logger.trace("accept remote message:[{}]", message);
        }

        final AsyncContext context = req.startAsync();
        EventEngineFactory.getProcessEngineByEndPointUrl(req.getRequestURI())
                .process(event, new InputEventCallback(req, resp, context));
    }

    private void writeResponse(AsyncContext context, byte[] message) {
        try {
            context.getResponse().getOutputStream().write(message);
        } catch (IOException e) {
            logger.error("write back message due to error", e);
        } finally {
            context.complete();
        }
    }

    private class InputEventCallback implements EventCallback<InputEvent> {

        public InputEventCallback(HttpServletRequest req, HttpServletResponse resp, AsyncContext context) {
            this.req = req;
            this.resp = resp;
            this.context = context;
        }

        HttpServletRequest req;
        HttpServletResponse resp;
        AsyncContext context;

        @Override
        public void preInvoke(com.baidu.ub.msoa.container.support.router.event.InputEvent event) {
            if (req.getRequestURI().contains(RouterConstants.RPC_ENDPOINT_FOR_NAVI10)) {
                event.message.meta.service = StringUtils.substringAfterLast(req.getRequestURI(), "/");
            }
        }

        @Override
        public void onSuccess(com.baidu.ub.msoa.container.support.router.event.InputEvent event) {
            if (logger.isTraceEnabled()) {
                logger.trace("write back message:[{}]", event.message);
            }

            resp.addHeader(RPC_STATUS, RouteStatus.SUCCESS.code + "");
            writeResponse(context, event.output);
        }

        @Override
        public void onFail(com.baidu.ub.msoa.container.support.router.event.InputEvent event, Throwable throwable) {
            logger.warn("process event:[{}] due to error:[{}]", event, ExceptionUtils.getStackTrace(throwable));

            resp.addHeader(RPC_STATUS, RouteStatus.PROCESS_ERROR.code + "");
            writeResponse(context, ExceptionUtils.getStackTrace(throwable).getBytes());
        }
    }
}
