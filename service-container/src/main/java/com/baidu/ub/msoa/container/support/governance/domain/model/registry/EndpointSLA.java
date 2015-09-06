package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import java.util.Objects;

/**
 * Created by pippo on 15/8/5.
 */
public class EndpointSLA extends ServiceSLA {

    private String hsot;
    private int port;

    public String getHsot() {
        return hsot;
    }

    public void setHsot(String hsot) {
        this.hsot = hsot;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EndpointSLA)) {
            return false;
        }
        EndpointSLA that = (EndpointSLA) o;
        return Objects.equals(port, that.port) && Objects.equals(hsot, that.hsot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hsot, port);
    }
}
