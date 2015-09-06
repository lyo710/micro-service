package com.baidu.ub.msoa.container.support.rpc;

/**
 * Created by pippo on 15/9/5.
 */
public interface RESTConstants extends HttpConstants {

    String REST_ENDPOINT_PATH = "/navi2/rest/*";

    // http://host:port/navi2/rest/{provider}/{service}/{version}/{method}
    String REST_ENDPOINT_URL = "http://%s:%s/navi2/rest/%s/%s/%s/%s";

    String CONTENT_TYPE_NAME = "Content-Type";

}
