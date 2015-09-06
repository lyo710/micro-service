package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import java.util.Objects;

/**
 * Created by pippo on 15/8/10.
 */
public class RegisterInfo {

    public RegisterInfo() {
    }

    public RegisterInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public RegisterInfo(ServiceInfo serviceInfo, boolean accept, String error) {
        this.serviceInfo = serviceInfo;
        this.accept = accept;
        this.error = error;
    }

    private ServiceInfo serviceInfo;
    private boolean accept;
    private String error;

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format("RegisterInfo{'super'=%s, 'serviceInfo'=%s, 'accept'=%s, 'error'=%s}",
                             super.toString(),
                             serviceInfo,
                             accept,
                             error);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegisterInfo)) {
            return false;
        }
        RegisterInfo info = (RegisterInfo) o;
        return Objects.equals(accept, info.accept) && Objects.equals(serviceInfo, info.serviceInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceInfo, accept);
    }
}
