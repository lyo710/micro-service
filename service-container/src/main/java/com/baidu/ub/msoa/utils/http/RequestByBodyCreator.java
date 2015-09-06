package com.baidu.ub.msoa.utils.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

/**
 * Created by pippo on 14-5-25.
 */
public class RequestByBodyCreator implements RequestCreator {

    public RequestByBodyCreator(String url, byte[] body) {
        this.url = url;
        this.body = body;
    }

    final String url;
    final byte[] body;

    @Override
    public HttpRequestBase create() throws Exception {
        HttpPost post = new HttpPost(url);
        post.setConfig(CONFIG);
        post.setEntity(new ByteArrayEntity(body));
        return post;
    }

    public static void main(String[] args) {
        System.out.println(RequestConfig.DEFAULT);
    }

}
