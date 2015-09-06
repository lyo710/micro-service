package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;
import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pippo on 15/8/3.
 */
public class ServiceInfo extends BundleServiceMetaInfo {

    public ServiceInfo() {

    }

    public ServiceInfo(int provider, String service, int version, String method, RouteStrategy routeStrategy) {
        super(provider, service, version, method, routeStrategy);
    }

    public void isConflict(ServiceInfo submit) throws ContactConflictException {

        /* 是否缩小了消费者范围 */
        for (String acceptConsumer : acceptConsumers) {
            if (!submit.acceptConsumers.contains(acceptConsumer)) {
                throw new ContactConflictException(String.format("accepted consumer:[%s] can not be removed",
                        acceptConsumer));
            }
        }

        /* contact是否兼容 */
        contact.isConflict(submit.contact);
    }

    protected Boolean available;
    protected ServiceSLA sla;
    protected Set<EndpointSLA> endpointSLA = new HashSet<>();
    protected Set<String> acceptConsumers = new HashSet<>();
    protected ServiceContact contact;
    protected APIDocument document;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public ServiceSLA getSla() {
        return sla;
    }

    public void setSla(ServiceSLA sla) {
        this.sla = sla;
    }

    public Set<EndpointSLA> getEndpointSLA() {
        return endpointSLA;
    }

    public void setEndpointSLA(Set<EndpointSLA> endpointSLA) {
        this.endpointSLA = endpointSLA;
    }

    public Set<String> getAcceptConsumers() {
        return acceptConsumers;
    }

    public void setAcceptConsumers(Set<String> acceptConsumers) {
        this.acceptConsumers = acceptConsumers;
    }

    public ServiceContact getContact() {
        return contact;
    }

    public void setContact(ServiceContact contact) {
        this.contact = contact;
    }

    public APIDocument getDocument() {
        return document;
    }

    public void setDocument(APIDocument document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return String.format("ServiceInfo{'super'=%s, 'available'=%s, 'sla'=%s, 'endpointSLA'=%s, "
                        + "'acceptConsumers'=%s, 'contact'=%s, 'document'=%s}",
                super.toString(),
                available,
                sla,
                endpointSLA,
                acceptConsumers,
                contact,
                document);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceInfo)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ServiceInfo that = (ServiceInfo) o;
        return Objects.equals(contact, that.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contact);
    }
}
