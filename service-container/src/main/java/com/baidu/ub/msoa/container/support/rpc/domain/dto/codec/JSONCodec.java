package com.baidu.ub.msoa.container.support.rpc.domain.dto.codec;

import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.baidu.ub.msoa.utils.JSONUtil;

/**
 * Created by pippo on 15/9/2.
 */
public class JSONCodec implements Codec {

    @Override
    public String getCodecType() {
        return CodecType.JSON.type;
    }

    @Override
    public <T> T decode(byte[] message, Class<T> clazz) {
        return JSONUtil.toObject(message, clazz);
    }

    @Override
    public byte[] encode(Object message) {
        return JSONUtil.toBytes(message);
    }
}
