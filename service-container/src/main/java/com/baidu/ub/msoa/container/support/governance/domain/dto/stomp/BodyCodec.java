package com.baidu.ub.msoa.container.support.governance.domain.dto.stomp;

import com.baidu.ub.msoa.utils.proto.ProtostuffReflect;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by pippo on 15/8/23.
 */
public class BodyCodec {

    public static String encodeBase64(Object body) {
        return Base64.encodeBase64String(ProtostuffReflect.encode(body));
    }

    public static <T> T decodeBase64(String base64String, Class<T> clazz) {
        return base64String != null ? ProtostuffReflect.decode(Base64.decodeBase64(base64String), clazz) : null;
    }

}
