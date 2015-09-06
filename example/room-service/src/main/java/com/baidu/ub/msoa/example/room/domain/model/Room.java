package com.baidu.ub.msoa.example.room.domain.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
public class Room {

    private long id;
    private int code;
    private Teacher teacher;
    private Set<Student> students;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        if (students == null) {
            students = new HashSet<>();
        }

        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return String.format("Room{'id'=%s, 'code'=%s, 'teacher'=%s, 'students'=%s}", id, code, teacher, students);
    }
}
