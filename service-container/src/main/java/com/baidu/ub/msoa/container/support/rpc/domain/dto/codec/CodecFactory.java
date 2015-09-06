package com.baidu.ub.msoa.container.support.rpc.domain.dto.codec;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/9/2.
 */
public class CodecFactory {

    private static Map<CodecType, Codec> codecs = new HashMap<>();

    static {
        codecs.put(CodecType.JSON, new JSONCodec());
        codecs.put(CodecType.PROTO, new ProtoCodec());
    }

    public static Codec getCodec(CodecType codecType) {
        return codecs.get(codecType);
    }

    public static Codec getCodec(String contentType) {
        return codecs.get(CodecType.from(contentType));
    }

    public static Codec getCodec(int code) {
        return codecs.get(CodecType.from(code));
    }

    public enum CodecType {

        JSON("application/json", 1),

        PROTO("application/proto", 2);

        CodecType(String type, int code) {
            this.type = type;
            this.code = code;
        }

        public final String type;
        public final int code;

        public static CodecType from(String contectType) {
            switch (contectType) {
                case "application/json":
                    return JSON;
                case "application/proto":
                    return PROTO;
                default:
                    return JSON;
            }
        }

        public static CodecType from(int code) {
            switch (code) {
                case 1:
                    return JSON;
                case 2:
                    return PROTO;
                default:
                    return JSON;
            }
        }

        @Override
        public String toString() {
            return type;
        }
    }

}
