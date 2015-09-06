package com.baidu.ub.msoa.container.support.rpc.outbound;

import com.baidu.ub.msoa.container.support.rpc.RPCStatus;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCArguments;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResult;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/9/5.
 */
@Component
public class LocalMethodOutbound {

    private CodecType codecType = CodecType.PROTO;

    public RPCResponse route(MethodExecutor executor, RPCRequest request) {
        RPCArguments arguments = request.arguments;

        if (arguments.codecType <= 0) {
            arguments.codecType = codecType.code;
        }

        if (arguments.payload == null) {
            arguments.args2bytes();
        }

        Object result = executor.execute(arguments.bytes2args(executor.getParameterTypes()));
        return new RPCResponse(request.id, RPCStatus.SUCCESS.code, new RPCResult(arguments.codecType, result));
    }

}
