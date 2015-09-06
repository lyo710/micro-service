package com.baidu.ub.msoa.example.account.domain.model;

import java.util.Objects;

/**
 * Created by pippo on 15/8/20.
 */
public class Account {

    private long id;
    private String name;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Account{'id'=%s, 'name'=%s, 'password'=%s}", id, name, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name) && Objects.equals(password,
                account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }
}
