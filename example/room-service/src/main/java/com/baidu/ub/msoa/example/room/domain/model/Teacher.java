package com.baidu.ub.msoa.example.room.domain.model;

/**
 * Created by pippo on 15/8/20.
 */
public class Teacher extends Member {

    private Level level;
    private String description;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Teacher{'super'=%s, 'level'=%s, 'description'=%s}", super.toString(), level, description);
    }
}
