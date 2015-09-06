package com.baidu.ub.msoa.container.support.rpc.domain.dto.codec;

/**
 * Created by pippo on 15/9/1.
 */
public interface Decoder {

    <T> T decode(byte[] message, Class<T> clazz);

}
