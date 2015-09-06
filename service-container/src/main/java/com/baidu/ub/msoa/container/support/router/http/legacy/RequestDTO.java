package com.baidu.ub.msoa.container.support.router.http.legacy;

/**
 * <tt>Navi 1.0</tt>的请求DTO
 *
 * @author zhangxu
 */
public class RequestDTO {

    /**
     * 请求追踪trace id
     */
    private long traceId;

    /**
     * 请求方法名称
     */
    private String method;

    /**
     * 请求参数数组
     */
    private Object[] parameters;

    /**
     * 请求方法参数类名数组
     *
     * @see ArgumentTypeHelper#getArgsTypeNameArray(Class[])
     */
    private String[] paramterTypes;

    /**
     * 用于权限校验的appId
     */
    private String appId;

    /**
     * 用于权限校验的token
     */
    private String token;

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParamterTypes() {
        return paramterTypes;
    }

    public void setParamterTypes(String[] paramterTypes) {
        this.paramterTypes = paramterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
