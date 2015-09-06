package com.baidu.ub.msoa.example.account.domain.model;

import java.util.List;

/**
 * Created by pippo on 15/8/20.
 */
public class Authority {

    private long id;
    private String name;
    private List<String> tags;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format("Authority{'id'=%s, 'name'=%s, 'tags'=%s}", id, name, tags);
    }
}
