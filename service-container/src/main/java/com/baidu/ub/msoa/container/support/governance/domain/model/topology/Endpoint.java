package com.baidu.ub.msoa.container.support.governance.domain.model.topology;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by pippo on 15/8/3.
 */
public class Endpoint extends BundleServiceMetaInfo {

    public Endpoint() {

    }

    public Endpoint(int provider, String service, int version, String method, String host, int port) {
        super(provider, service, version, method, RouteStrategy.switchover);
        this.host = host;
        this.port = port;
    }

    private String host;
    private int port;
    private float priority = 1;
    private EndpointStatus status;
    private EndpointStatistics statistics;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public float getPriority() {
        return priority;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }

    public EndpointStatus getStatus() {
        return status;
    }

    public void setStatus(EndpointStatus status) {
        this.status = status;
    }

    public EndpointStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(EndpointStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endpoint)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(provider, endpoint.provider)
                && Objects.equals(serviceIdentity, endpoint.serviceIdentity)
                && Objects.equals(host, endpoint.host)
                && Objects.equals(port, endpoint.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), host, port);
    }

    @Override
    public String toString() {
        return String.format("Endpoint{'super'=%s, 'host'=%s, 'port'=%s, 'priority'=%s, 'status'=%s}",
                super.toString(),
                host,
                port,
                priority,
                status);
    }

    public static class EndpointComparator implements Comparator<Endpoint> {

        @Override
        public int compare(Endpoint o1, Endpoint o2) {

            /* 优先级高的节点排在更前面 */
            if (o1.priority > o2.priority) {
                return -1;
            } else if (o1.priority < o2.priority) {
                return 1;
            }

            if (o1.statistics != null && o2.statistics != null) {
                return o1.statistics.compareTo(o2.statistics);
            }

            return 0;
        }
    }
}
