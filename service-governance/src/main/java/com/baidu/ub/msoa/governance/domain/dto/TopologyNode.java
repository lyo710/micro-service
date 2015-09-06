package com.baidu.ub.msoa.governance.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/30.
 */
public class TopologyNode {

    private String id;
    private String name;
    private Object data;
    private List<TopologyNode> children = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<TopologyNode> getChildren() {
        return children;
    }

    public void setChildren(List<TopologyNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return String.format("TopologyNode{'id'=%s, 'name'=%s, 'data'=%s}", id, name, data);
    }
}
