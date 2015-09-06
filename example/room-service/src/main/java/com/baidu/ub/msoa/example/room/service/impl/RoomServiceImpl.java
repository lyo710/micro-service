package com.baidu.ub.msoa.example.room.service.impl;

import com.baidu.ub.msoa.example.room.domain.model.Room;
import com.baidu.ub.msoa.example.room.domain.model.Student;
import com.baidu.ub.msoa.example.room.domain.model.Teacher;
import com.baidu.ub.msoa.example.room.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by pippo on 15/8/20.
 */
@Service
public class RoomServiceImpl implements RoomService {

    private static Map<Long, Room> rooms = new HashMap<>();

    @Override
    public long create(Room room) {
        long id = System.nanoTime();
        System.out.println(id);
        room.setId(id);
        rooms.put(id, room);
        return id;
    }

    @Override
    public Set<Student> loadStudentByRoom(long roomId) {
        Room room = rooms.get(roomId);
        return room == null ? new HashSet<Student>() : room.getStudents();
    }

    @Override
    public Teacher[] loadTeacherByRoom(long roomId) {
        Room room = rooms.get(roomId);
        return room != null && room.getTeacher() != null ? new Teacher[] { room.getTeacher() } : new Teacher[0];
    }

    @Override
    public void setTeacher(long roomId, Teacher teacher) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.setTeacher(teacher);
        }
    }

    @Override
    public void addStudent(long roomId, Student student) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.getStudents().add(student);
        }
    }
}
