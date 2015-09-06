/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */
/**
 * project : ctu-utils
 * user created : pippo
 * date created : 2013-4-17 - 下午12:11:51
 */
package com.baidu.ub.msoa.utils.http;

import com.google.common.io.ByteStreams;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * AbstractHttpHelper
 *
 * @author pippo
 * @since 2013-4-17
 */
public abstract class AbstractHttpHelper {

    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

    public static class GZIPRequestInterceptor implements HttpRequestInterceptor {

        public static final GZIPRequestInterceptor DEFAULT = new GZIPRequestInterceptor();

        public static final String GZIP = "gzip";

        public static final String ACCEPT_ENCODING = "Accept-Encoding";

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            if (!request.containsHeader(ACCEPT_ENCODING)) {
                request.addHeader(ACCEPT_ENCODING, GZIP);
            }
        }

    }

    public static class GZIPResponseInterceptor implements HttpResponseInterceptor {

        public static final GZIPResponseInterceptor DEFAULT = new GZIPResponseInterceptor();

        @Override
        public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return;
            }

            Header header = entity.getContentEncoding();
            if (header == null) {
                return;
            }

            HeaderElement[] codecs = header.getElements();

            for (int i = 0; i < codecs.length; i++) {
                if (codecs[i].getName().equalsIgnoreCase(GZIPRequestInterceptor.GZIP)) {
                    logger.debug("the content encoding is:{}, use gzip decompressing", Arrays.toString(codecs));
                    response.setEntity(new GzipDecompressingEntity(entity));
                    return;
                }
            }
        }

    }

    public static class ResponseToString implements ResponseHandler<String> {

        public static final ResponseToString DEFAULT = new ResponseToString(CharEncoding.UTF_8);

        public ResponseToString(String charset) {
            this.charset = charset;
        }

        private String charset;

        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            return new String(ByteStreams.toByteArray(response.getEntity().getContent()), charset);
        }
    }

    public <T> T doPost(final String url, final byte[] body, ResponseHandler<T> handler) {
        return executeRequest(new RequestByBodyCreator(url, body), handler);
    }

    public <T> T doPost(final String url, final Map<String, ?> parameters, ResponseHandler<T> handler) {
        return executeRequest(new RequestByParametersCreator(url, parameters), handler);
    }

    public <T> T doGet(final String url, ResponseHandler<T> handler) {
        return executeRequest(new RequestByURLCreator(url), handler);
    }

    protected abstract HttpClient getHttpClient();

    public <T> T executeRequest(RequestCreator creator, ResponseHandler<T> handler) {

        try {
            return executeRequest(creator.create(), handler);
        } catch (Exception e) {
            throw new HttpRuntimeException(e);
        }
    }

    public <T> T executeRequest(HttpRequestBase request, final ResponseHandler<T> handler) {

        try {
            return getHttpClient().execute(request, handler);
        } catch (Exception e) {
            throw new HttpRuntimeException(e);
        } finally {
            request.releaseConnection();
        }
    }

}