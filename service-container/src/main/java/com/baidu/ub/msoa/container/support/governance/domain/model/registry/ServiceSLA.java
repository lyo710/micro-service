package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pippo on 15/8/4.
 */
public class ServiceSLA {

    private int currency = 1000;
    private int latency = 100;
    private int qps = 10000;

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

    public int getQps() {
        return qps;
    }

    public void setQps(int qps) {
        this.qps = qps;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("currency", currency)
                .append("latency", latency)
                .append("qps", qps)
                .toString();
    }
}
