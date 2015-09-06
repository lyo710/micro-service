package com.baidu.ub.msoa.governance;

import com.baidu.ub.msoa.container.support.rpc.RPCStatus;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.container.support.rpc.outbound.HttpRESTOutbound;
import com.baidu.ub.msoa.governance.domain.model.AppInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by pippo on 15/9/2.
 */
public class RESTClientTest {

    private HttpRESTOutbound outbound = new HttpRESTOutbound();

    @Test
    public void getAllProvider() {
        CodecType codecType = CodecType.JSON;

        RPCRequest request = new RPCRequest();
        request.arguments = new RPCArguments(codecType, null);

        RPCResponse response =
                outbound.route("127.0.0.1", 8156, 2, "serviceTopologyService", 1, "getAllProvider", request, codecType);
        Assert.assertTrue(response.status == RPCStatus.SUCCESS.code);

        Integer[] providers = response.result.bytes2value(Integer[].class);
        System.out.println(Arrays.toString(providers));
        Assert.assertTrue(providers != null);
    }

    @Test
    public void persistAppInfo() {

        AppInfo info = new AppInfo();
        info.setId(5);
        info.setName("test");

        CodecType codecType = CodecType.PROTO;
        RPCRequest request = new RPCRequest();
        request.arguments = new RPCArguments(codecType, info);

        RPCResponse response = outbound.route("127.0.0.1", 8156, 2, "appInfoService", 1, "persist", request, codecType);

        Assert.assertTrue(response.status == RPCStatus.SUCCESS.code);
    }

}
