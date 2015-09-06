package com.baidu.ub.msoa.container.support.governance.domain.dto;

/**
 * Created by pippo on 15/8/23.
 */
public class ServiceTopologyRequest extends BaseRequest {

    public ServiceTopologyRequest() {

    }

    public ServiceTopologyRequest(int provider, String service, int version, String method) {
        this.provider = provider;
        this.service = service;
        this.version = version;
        this.method = method;
    }

    private int provider;
    private String service;
    private int version;
    private String method;

    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return String.format("ServiceTopologyRequest{'provider'=%s, 'service'=%s, 'version'=%s, 'method'=%s}",
                provider,
                service,
                version,
                method);
    }
}
