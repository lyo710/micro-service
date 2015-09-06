package com.baidu.ub.msoa.container.support.rpc.domain.dto.codec;

import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.utils.proto.ProtostuffReflect;

/**
 * Created by pippo on 15/9/2.
 */
public class ProtoCodec implements Codec {

    @Override
    public String getCodecType() {
        return CodecType.PROTO.type;
    }

    @Override
    public <T> T decode(byte[] message, Class<T> clazz) {
        return ProtostuffReflect.decode(message, clazz);
    }

    @Override
    public byte[] encode(Object message) {
        return ProtostuffReflect.encode(message);
    }
}
