package com.baidu.ub.msoa.example.room.service;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.CollectionElementType;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.example.room.domain.model.Room;
import com.baidu.ub.msoa.example.room.domain.model.Student;
import com.baidu.ub.msoa.example.room.domain.model.Teacher;

import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
@BundleService(name = "roomService", version = 20150901)
public interface RoomService {

    /**
     * 创建教室
     *
     * @param room
     * @return roomId
     */
    long create(Room room);

    /**
     * load room students
     *
     * @param roomId
     * @return students
     */
    @CollectionElementType(Student.class)
    Set<Student> loadStudentByRoom(long roomId);

    /**
     * load room teacher
     *
     * @param roomId
     * @return teachers
     */
    Teacher[] loadTeacherByRoom(long roomId);

    /**
     * set room teacher
     *
     * @param roomId
     * @param teacher
     */
    void setTeacher(long roomId, Teacher teacher);

    /**
     * add student to room
     *
     * @param roomId
     * @param student
     */
    void addStudent(long roomId, Student student);

}
