package com.baidu.ub.msoa.container.support.rpc.outbound;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.rpc.RESTConstants;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResult;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.utils.http.HttpHelper;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by pippo on 15/9/4.
 */
@Component
public class HttpRESTOutbound implements RESTConstants {

    private CodecType defaultCodecType = CodecType.PROTO;

    public RPCResponse route(Endpoint endpoint, RPCRequest request) {
        return this.route(endpoint, request, defaultCodecType);
    }

    public RPCResponse route(Endpoint endpoint, RPCRequest request, CodecType codecType) {
        return this.route(endpoint.getHost(),
                endpoint.getPort(),
                endpoint.provider,
                endpoint.service,
                endpoint.version,
                endpoint.method,
                request,
                codecType);
    }

    public RPCResponse route(String host,
            int port,
            int provider,
            String service,
            int version,
            String method,
            final RPCRequest request,
            final CodecType codecType) {

        // http://host:port/navi2/rest/{provider}/{service}/{version}/{method}
        HttpPost post = new HttpPost(String.format(REST_ENDPOINT_URL, host, port, provider, service, version, method));

        post.addHeader(CONTENT_TYPE_NAME, codecType.type);
        post.addHeader(NAVI2_RPC_REQUEST_ID_NAME, Long.toString(request.id));
        post.addHeader(NAVI2_RPC_TRACE_ID_NAME, request.header.trace.traceId);

        final RPCArguments arguments = request.arguments;
        arguments.codecType = codecType.code;
        arguments.args2bytes();
        post.setEntity(new ByteArrayEntity(arguments.payload));

        return HttpHelper.get().executeRequest(post, new ResponseHandler<RPCResponse>() {

            @Override
            public RPCResponse handleResponse(HttpResponse response) throws IOException {
                RPCResponse rpcResponse = new com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse();
                rpcResponse.id = request.id;
                rpcResponse.status = response.getStatusLine().getStatusCode();
                rpcResponse.result = new RPCResult();
                rpcResponse.result.codecType = codecType.code;
                rpcResponse.result.payload = ByteStreams.toByteArray(response.getEntity().getContent());
                return rpcResponse;
            }

        });
    }

}
