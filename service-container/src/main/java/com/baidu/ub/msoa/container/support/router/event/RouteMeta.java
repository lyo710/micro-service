package com.baidu.ub.msoa.container.support.router.event;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pippo on 15/7/20.
 */
public class RouteMeta extends BundleServiceMetaInfo {

    public RouteMeta() {

    }

    public RouteMeta(int provider, String service, int version, String method, RouteStrategy routeStrategy) {
        super(provider, service, version, method, routeStrategy);
    }

    public RouteMeta(String traceId,
            String sequence,
            String signature,
            int provider,
            String service,
            int version,
            String method,
            RouteStrategy routeStrategy) {

        super(provider, service, version, method, routeStrategy);
        this.traceId = traceId;
        this.sequence = sequence;
        this.signature = signature;
    }

    public String traceId;
    public String sequence;
    public String signature;
    public int status;
    public String userId;
    public long timestamp;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("traceId", traceId)
                .append("sequence", sequence)
                .append("signature", signature)
                .append("serviceIdentity", serviceIdentity)
                .toString();
    }
}
