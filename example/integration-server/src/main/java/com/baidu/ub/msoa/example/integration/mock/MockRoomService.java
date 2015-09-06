package com.baidu.ub.msoa.example.integration.mock;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Room;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Student;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Teacher;
import com.baidu.ub.msoa.example.room.service.p_1.v_20150901.RoomService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/8/28.
 */

@BundleService(name = "roomService", version = 20150901)
@Service
@Profile("mock")
public class MockRoomService implements RoomService {

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
    public Student[] loadStudentByRoom(long roomId) {
        Room room = rooms.get(roomId);
        return room == null ? new Student[0] : room.getStudents();
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
            room.setStudents(ArrayUtils.add(room.getStudents(), student));
        }
    }
}
