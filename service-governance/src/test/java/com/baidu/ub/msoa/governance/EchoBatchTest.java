package com.baidu.ub.msoa.governance;

import com.baidu.ub.msoa.container.support.rpc.RPCStatus;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.container.support.rpc.outbound.HttpRESTOutbound;
import com.baidu.ub.msoa.utils.thread.WorkStealingPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by pippo on 15/9/2.
 */
public class EchoBatchTest {

    private HttpRESTOutbound outbound = new HttpRESTOutbound();

    @Test
    public void single() {
        CodecType codecType = CodecType.PROTO;

        RPCRequest request = new RPCRequest();
        request.arguments = new RPCArguments(codecType, "pippo");

        long total = 0;

        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            RPCResponse response = outbound.route("127.0.0.1", 8156, 2, "echoService", 1, "echo", request, codecType);
            Assert.assertTrue(response.status == RPCStatus.SUCCESS.code);
            total += (System.nanoTime() - start);
        }

        System.out.println("single thread:" + ((double) total / (double) 1000000 / (double) 100000));

    }

    @Test
    public void multi() throws InterruptedException {
        final CodecType codecType = CodecType.PROTO;
        final RPCRequest request = new RPCRequest();
        request.arguments = new RPCArguments(codecType, "pippo");

        final AtomicLong total = new AtomicLong(0);

        Collection<Callable<Object>> tasks = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            tasks.add(new Callable<Object>() {

                @Override
                public Object call() throws Exception {
                    long start = System.nanoTime();
                    RPCResponse response =
                            outbound.route("127.0.0.1", 8156, 2, "echoService", 1, "echo", request, codecType);
                    Assert.assertTrue(response.status == RPCStatus.SUCCESS.code);
                    total.addAndGet(System.nanoTime() - start);
                    return null;
                }

            });

        }

        WorkStealingPool.create().invokeAll(tasks);

        System.out.println("single thread:" + ((double) total.get() / (double) 1000000 / (double) 100000));

    }

}
