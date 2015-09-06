package com.baidu.ub.msoa.container.support.router.http.legacy;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

/**
 * 方法参数工具类
 *
 * @author zhangxu04
 */
public class ArgumentTypeHelper {

    /**
     * Get parameter type name array from a method
     *
     * @param argsTypes 参数类型
     *
     * @return 参数类型名称数组，如果没有参数返回空数组，否则返回参数名数组
     */
    public static String[] getArgsTypeNameArray(Class<?>[] argsTypes) {
        String[] argsTypeArray = null;
        if (argsTypes != null) {
            argsTypeArray = new String[argsTypes.length];
            for (int i = 0; i < argsTypes.length; i++) {
                argsTypeArray[i] = argsTypes[i].getName();
            }
        }
        return argsTypeArray;
    }

    /**
     * Get parameter type name string from a arg types string array
     *
     * @param argTypes 参数类型数组
     *
     * @return 参数类型名称字符串
     */
    public static String getArgsTypeName(String[] argTypes) {
        if (argTypes != null) {
            return Joiner.on(",").join(argTypes);
        }
        return StringUtils.EMPTY;
    }

}
