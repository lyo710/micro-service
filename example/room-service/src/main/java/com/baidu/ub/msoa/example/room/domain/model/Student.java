package com.baidu.ub.msoa.example.room.domain.model;

/**
 * Created by pippo on 15/8/20.
 */
public class Student extends Member {

    private Gender gender;
    private String description;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Student{'super'=%s, 'gender'=%s, 'description'=%s}",
                super.toString(),
                gender,
                description);
    }


}
