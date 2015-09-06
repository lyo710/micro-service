package com.baidu.ub.msoa.container.support.governance.domain.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pippo on 15/8/3.
 */
public class ServiceRegisterRequest extends BaseRequest {

    public ServiceRegisterRequest() {

    }

    public ServiceRegisterRequest(ContactType contactType, String contact) {
        this.contactType = contactType;
        this.contact = contact;
    }

    private Set<String> acceptConsumers = new HashSet<>();
    private ContactType contactType;
    private String contact;
    private int currency = 1000;
    private int latency = 100;
    private int qps = 10000;

    public Set<String> getAcceptConsumers() {
        return acceptConsumers;
    }

    public void setAcceptConsumers(Set<String> acceptConsumers) {
        this.acceptConsumers = acceptConsumers;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

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
        return String.format("ServiceRegisterRequest{'contact'=%s}", contact.replace("\n", " "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceRegisterRequest)) {
            return false;
        }
        ServiceRegisterRequest request = (ServiceRegisterRequest) o;
        return Objects.equals(contact, request.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contact);
    }
}
