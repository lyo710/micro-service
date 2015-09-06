package com.baidu.ub.msoa.utils;

import java.util.UUID;

/**
 * id生成器
 *
 * @author zhangxu
 */
public class IdGenerator {

    /**
     * 生成一个Unique ID， 默认使用JDK自带的UUID
     * <p/>
     * 也可以使用：
     * <pre>
     * return UUIDGenerator.getInstance().generateTimeBasedUUID().toString();
     * return Math.abs(new Random(System.currentTimeMillis()).nextInt());
     * </pre>
     *
     * @return
     */
    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

}
