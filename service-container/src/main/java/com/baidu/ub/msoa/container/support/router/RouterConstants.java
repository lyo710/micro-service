package com.baidu.ub.msoa.container.support.router;

/**
 * Created by pippo on 15/7/19.
 */
public interface RouterConstants {

    /**
     * route endpoint timeout
     */
    int ROUTE_TIMEOUT = 30 * 1000;

    /**
     * rpc end point path
     */
    String RPC_ENDPOINT = "/navi/rpc/service";

    /**
     * rpc end point path for legacy framework <tt>navi 1.0</tt>
     */
    String RPC_ENDPOINT_FOR_NAVI10 = "/service_api";

    /**
     * rpc end point url patterns for legacy framework <tt>navi 1.0</tt>
     */
    String RPC_ENDPOINT_FOR_NAVI10_URL_PATTERNS = RPC_ENDPOINT_FOR_NAVI10 + "/*";

    /**
     * rpc trace id
     */
    String RPC_TRACE_ID = "rpc.trace.id";

    /**
     * rpc trace sequence
     */
    String RPC_SEQUENCE = "rpc.sequence";

    /**
     * rpc message signature
     */
    String RPC_SIGNATURE = "rpc.signature";

    /**
     * rpc service provider
     */
    String RPC_PROVIDER = "rpc.provider";

    /**
     * rpc service name
     */
    String RPC_SERVICE = "rpc.service";

    /**
     * rpc service version
     */
    String RPC_VERSION = "rpc.version";

    /**
     * rpc service method
     */
    String RPC_METHOD = "rpc.method";

    /**
     * rpc service process status
     */
    String RPC_STATUS = "rpc_status";

    /**
     * rpc service meta
     */
    String RPC_META = "rpc.http.meta";

    /**
     * rpc service body
     */
    String RPC_BODY = "rpc.http.body";

    /**
     * rpc content-type <br/>
     * Note: This is specially for compatibility of <tt>navi 1.0</tt>
     */
    String RPC_CONTENT_TYPE = "Content-Type";

    /**
     * binary packet length occupy size
     */
    int PACKET_LENGTH_SIZE = 4;

    /**
     * binary packet header occupy size for routeId
     */
    int H_ROUTE_ID_SIZE = 36;

    /**
     * binary packet header occupy size for meta length
     */
    int H_META_LENGTH_SIZE = 4;

    /**
     * binary packet header occupy size for body length
     */
    int H_BODY_LENGTH_SIZE = 4;

    /**
     * binary packet header occupy size
     */
    int HEADER_SIZE = H_ROUTE_ID_SIZE + H_META_LENGTH_SIZE + H_BODY_LENGTH_SIZE;
}
