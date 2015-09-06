package com.baidu.ub.msoa.container.support.governance;

import com.baidu.ub.msoa.container.support.governance.domain.dto.BaseResponse;
import com.baidu.ub.msoa.utils.JSONUtil;
import com.baidu.ub.msoa.utils.http.HttpHelper;
import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.util.Random;

/**
 * Created by pippo on 15/8/23.
 */
public class HttpClient {

    public HttpClient(String[] addresses) {
        this.addresses = addresses;
    }

    private String[] addresses;

    public <T extends BaseResponse> T post(Object request, final Class<T> clazz) {
        HttpPost post = new HttpPost(registerURL());
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new ByteArrayEntity(JSONUtil.toBytes(request)));

        return HttpHelper.get().executeRequest(post, new ResponseHandler<T>() {

            @Override
            public T handleResponse(HttpResponse response) throws IOException {
                T resp;
                int statusCode = response.getStatusLine().getStatusCode();

                switch (statusCode) {
                    case 200:
                        resp = JSONUtil.toObject(response.getEntity().getContent(), clazz);
                        break;
                    case 404:
                        resp = error(clazz, response.getStatusLine().getReasonPhrase());
                        break;
                    default:
                        resp = error(clazz, new String(ByteStreams.toByteArray(response.getEntity().getContent())));
                        break;
                }

                return resp;
            }
        });

    }

    private <T extends BaseResponse> T error(Class<T> clazz, String error) {
        try {
            T t = clazz.newInstance();
            t.setSuccess(false);
            t.setError(error);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Random random = new Random();

    private String registerURL() {
        return addresses[random.nextInt(addresses.length)];
    }

}
