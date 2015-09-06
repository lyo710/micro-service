package com.baidu.ub.msoa.container.support.router.http.legacy;

/**
 * <tt>Navi 1.0</tt>的响应DTO
 *
 * @author zhangxu
 */
public class ResponseDTO {

    /**
     * 请求追踪trace id
     */
    private long traceId;

    /**
     * 请求结果对象
     */
    private Object result;

    /**
     * 状态码
     *
     * @see com.sirius.component.container.support.router.http.legacy.NaviLegacyConstants.StatusCode
     */
    private int status;

    /**
     * 请求发生异常时候的异常对象
     */
    private Throwable error;

    /**
     * Creates a new instance of ResponseDTO.
     */
    public ResponseDTO() {

    }

    /**
     * 构建响应DTO
     *
     * @param status 状态码
     * @param error  异常
     *
     * @return ResponseDTO
     */
    public static ResponseDTO build(int status, Throwable error) {
        ResponseDTO result = new ResponseDTO();
        result.setStatus(status);
        result.setError(error);
        return result;
    }

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

}
