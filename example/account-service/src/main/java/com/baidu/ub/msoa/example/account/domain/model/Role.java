package com.baidu.ub.msoa.example.account.domain.model;

import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
public class Role {

    private long id;
    private String name;
    private Set<Authority> authorities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return String.format("Role{'id'=%s, 'name'=%s, 'authorities'=%s}", id, name, authorities);
    }
}
