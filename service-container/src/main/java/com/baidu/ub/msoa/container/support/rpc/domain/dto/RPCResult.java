package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.baidu.ub.msoa.container.support.rpc.RPCConstants;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;

import java.util.Arrays;

/**
 * Created by pippo on 15/9/2.
 */
public class RPCResult implements RPCConstants {

    public RPCResult() {

    }

    public RPCResult(CodecType codecType, Object value) {
        this.codecType = codecType.code;
        this.value = value;
        value2bytes();
    }

    public RPCResult(int codecType, Object value) {
        this.codecType = codecType;
        this.value = value;
        value2bytes();
    }

    public void value2bytes() {
        if (value == null) {
            this.payload = BYTES;
            return;
        }

        this.payload = CodecFactory.getCodec(codecType).encode(value);
    }

    public void value2bytes(Object value) {
        this.value = value;
        value2bytes();
    }

    @SuppressWarnings("unchecked")
    public <T> T bytes2value(Class<T> type) {
        if (payload == null || payload.length == 0) {
            return null;
        }

        this.value = CodecFactory.getCodec(codecType).decode(payload, type);
        return (T) this.value;
    }

    public int codecType = -1;
    public Object value;
    public byte[] payload;

    @Override
    public String toString() {
        return String.format("RPCResult{'codecType'=%s, 'value'=%s, 'payload'=%s}",
                codecType,
                value,
                Arrays.toString(payload));
    }
}
