package com.baidu.ub.msoa.container.support.router.http.legacy;

/**
 * <tt>Navi 1.0</tt>相关的常量
 *
 * @author zhangxu
 */
public class NaviLegacyConstants {

    /**
     * {@link ResponseDTO#getStatus()}返回的状态码含义
     */
    public static class StatusCode {

        /**
         * 成功
         */
        public static final int RPC_OK = 0;

        /**
         * 失败<br/>
         * 通常是框架造成的，例如序列化失败等
         */
        public static final int RPC_FAIL = 1;

        /**
         * 系统内部错误<br/>
         * 通常是业务逻辑造成的，不属于框架问题
         */
        public static final int SYS_ERROR = 2;

    }

    /**
     * 序列化支持协议
     * <ul>
     * <li>PROTOSTUFF:application/baidu.protostuff</li>
     * <li>PROTOBUF:application/baidu.protobuf</li>
     * <li>JSON:application/baidu.json</li>
     * </ul>
     */
    public static enum NaviSerializeProtocol {

        /**
         * protostuff
         */
        PROTOSTUFF("application/baidu.protostuff"),

        /**
         * protobuf
         */
        PROTOBUF("application/baidu.protobuf"),

        /**
         * json
         */
        JSON("application/baidu.json");

        /**
         * content type名称
         */
        private String name;

        /**
         * Creates a new instance of NaviProtocol.
         *
         * @param name 序列化名称
         */
        private NaviSerializeProtocol(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
