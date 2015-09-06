package com.baidu.ub.msoa.example.account.domain.model;

import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
public class User extends Account {

    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return String.format("User{'super'=%s, 'roles'=%s}", super.toString(), roles);
    }


}
