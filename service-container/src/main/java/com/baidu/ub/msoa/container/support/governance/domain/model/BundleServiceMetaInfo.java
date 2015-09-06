package com.baidu.ub.msoa.container.support.governance.domain.model;

import com.baidu.ub.msoa.container.support.governance.domain.BundleServiceNameSpace;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.util.Objects;

/**
 * Created by pippo on 15/6/25.
 */
public class BundleServiceMetaInfo implements BundleServiceNameSpace {

    public static final String TO_STRING_FORMAT = "BundleServiceMetaInfo"
            + "{'provider'=%s, 'service'=%s, 'version'=%s, 'method'=%s, 'routeStrategy'=%s, 'serviceIdentity'=%s}";

    public BundleServiceMetaInfo() {

    }

    public BundleServiceMetaInfo(int provider,
            String service,
            int version,
            String method,
            RouteStrategy routeStrategy) {
        this.provider = provider;
        this.service = service;
        this.version = version;
        this.method = method;
        this.routeStrategy = routeStrategy;
        this.serviceIdentity = NameSpace.serviceIdentity(provider, service, version, method);
    }

    public int provider;
    public String service;
    public int version;
    public String method;
    public RouteStrategy routeStrategy;
    public String serviceIdentity;

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

    public RouteStrategy getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(RouteStrategy routeStrategy) {
        this.routeStrategy = routeStrategy;
    }

    public String getServiceIdentity() {
        return serviceIdentity;
    }

    public void setServiceIdentity(String serviceIdentity) {
        this.serviceIdentity = serviceIdentity;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_FORMAT, provider, service, version, method, routeStrategy, serviceIdentity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BundleServiceMetaInfo)) {
            return false;
        }
        BundleServiceMetaInfo that = (BundleServiceMetaInfo) o;
        return Objects.equals(provider, that.provider) && Objects.equals(serviceIdentity, that.serviceIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, serviceIdentity);
    }
}