package com.baidu.ub.msoa.container.support.rpc.domain.dto;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCTrace {

    public String traceId;
    public short spanId = 0;
    public int user = -1;
    public long timestamp = System.currentTimeMillis();

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public short getSpanId() {
        return spanId;
    }

    public void setSpanId(short spanId) {
        this.spanId = spanId;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("RPCTrace{'traceId'=%s, 'spanId'=%s, 'user'=%s, 'timestamp'=%s}",
                traceId,
                spanId,
                user,
                timestamp);
    }
}
