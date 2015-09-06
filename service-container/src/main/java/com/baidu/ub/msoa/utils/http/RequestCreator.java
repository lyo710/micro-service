package com.baidu.ub.msoa.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by pippo on 14-5-25.
 */
public interface RequestCreator {

    RequestConfig CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(1000)
            .setConnectTimeout(1000 * 5)
            .setSocketTimeout(1000 * 30)
            .build();

    HttpRequestBase create() throws Exception;

}
