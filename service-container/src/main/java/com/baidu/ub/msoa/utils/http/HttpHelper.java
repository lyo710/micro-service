/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : des-rss-fetcher
 * user created : pippo
 * date created : 2009-8-29 - 上午01:00:51
 */
package com.baidu.ub.msoa.utils.http;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HttpHelper
 *
 * @author pippo
 * @since 2009-8-29
 */
public final class HttpHelper extends AbstractHttpHelper {

    protected static HttpHelper instance = new HttpHelper();

    public static HttpHelper get() {
        return instance;
    }

    private HttpHelper() {
        httpClient = HttpClientBuilder.create()
                .setConnectionManager(getClientConnectionManager())
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.DEFAULT)
                .addInterceptorFirst(GZIPRequestInterceptor.DEFAULT)
                .addInterceptorLast(GZIPResponseInterceptor.DEFAULT)
                .build();
    }

    protected HttpClient httpClient;

    protected HttpClient getHttpClient() {
        return this.httpClient;
    }

    protected HttpClientConnectionManager getClientConnectionManager() {
        PoolingHttpClientConnectionManager clientConnectionManager =
                new PoolingHttpClientConnectionManager(5, TimeUnit.MINUTES);
        clientConnectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setBufferSize(4096)
                .setCharset(Consts.UTF_8)
                .build());
        clientConnectionManager.setDefaultSocketConfig(SocketConfig.custom().build());
        clientConnectionManager.setDefaultMaxPerRoute(Runtime.getRuntime().availableProcessors() * 1024);
        clientConnectionManager.setMaxTotal(Runtime.getRuntime().availableProcessors() * 1024 * 4);
        return clientConnectionManager;
    }

    /**
     * ********** post by parameters and handler **********
     */

    public <T> T doPost(String url, Map<String, ?> parameters, ResponseHandler<T> handler) {
        return super.doPost(url, parameters, handler);
    }

    public String doPost(String uri, Map<String, String> parameters, String charset) {
        return super.doPost(uri, parameters, new ResponseToString(charset));
    }

    public String doPost(String uri, Map<String, String> parameters) {
        return super.doPost(uri, parameters, ResponseToString.DEFAULT);
    }

    /**
     * *********** post by body and handler ******************
     */

    public <T> T doPost(String uri, String body, ResponseHandler<T> handler) {
        return super.doPost(uri, body.getBytes(), handler);
    }

    public String doPost(String uri, String body, String charset) {
        return super.doPost(uri, body.getBytes(), new ResponseToString(charset));
    }

    public String doPost(String uri, String body) {
        return super.doPost(uri, body.getBytes(), ResponseToString.DEFAULT);
    }

    /**
     * ****** getDefault by value *******************
     */

    public <T> T doGet(String url, ResponseHandler<T> handler) {
        return super.doGet(url, handler);
    }

    public String doGet(String uri, String charset) {
        return super.doGet(uri, new ResponseToString(charset));
    }

    public String doGet(String uri) {
        return super.doGet(uri, ResponseToString.DEFAULT);
    }

}
