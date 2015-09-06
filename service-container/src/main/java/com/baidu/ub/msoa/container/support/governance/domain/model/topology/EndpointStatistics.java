package com.baidu.ub.msoa.container.support.governance.domain.model.topology;

/**
 * Created by pippo on 15/8/4.
 */
public class EndpointStatistics implements Comparable<EndpointStatistics> {

    // 三分钟内平均并发
    private int currency = 0;
    // 三分钟内平均时延
    private int latency = 0;
    // 三分钟内失败次数
    private int errorCount = 0;

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    @Override
    public int compareTo(EndpointStatistics o) {
        /* 时延低的更靠前 */
        if (this.latency < o.latency) {
            return -1;
        } else if (this.latency > o.latency) {
            return 1;
        }

        /* 并发低的更靠前 */
        if (this.currency < o.currency) {
            return -1;
        } else if (this.currency > o.currency) {
            return 1;
        }

        /* 错误数少的更靠前 */
        if (this.errorCount < o.errorCount) {
            return -1;
        } else if (this.errorCount > o.errorCount) {
            return 1;
        }

        return 0;
    }
}
