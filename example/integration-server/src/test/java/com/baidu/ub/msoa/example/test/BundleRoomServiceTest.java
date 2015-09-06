package com.baidu.ub.msoa.example.test;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.container.test.BundleIntegrationTest;
import com.baidu.ub.msoa.container.test.BundleTestConstants;
import com.baidu.ub.msoa.example.integration.service.Echo;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Room;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Student;
import com.baidu.ub.msoa.example.room.domain.model.p_1.Teacher;
import com.baidu.ub.msoa.example.room.service.p_1.v_20150901.RoomService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by pippo on 15/8/20.
 */
@ActiveProfiles({ BundleTestConstants.PROFILE })
@BundleIntegrationTest(bundle = "integration-server")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BundleRoomServiceTest {

    @BundleService
    private RoomService roomService;

    @Resource
    private Echo echo;

    @Test
    public void inject() {
        Assert.assertNotNull(echo);
    }

    private static long roomId;

    @Test
    public void createRoom() {
        Room room = new Room();
        room.setCode(123);
        roomId = roomService.create(room);
        System.out.println(roomId);
        Assert.assertTrue(roomId > 0);
    }

    @Test
    public void loadStudent() {
        Room room = new Room();
        room.setCode(675);
        roomId = roomService.create(room);

        Student[] students = roomService.loadStudentByRoom(roomId);
        System.out.println(Arrays.toString(students));
        Assert.assertTrue(students == null || students.length == 0);

        Student student = new Student();
        student.setName("pippo");
        roomService.addStudent(roomId, student);
        students = roomService.loadStudentByRoom(roomId);
        System.out.println(Arrays.toString(students));
        Assert.assertTrue(students != null);
        Assert.assertTrue(students.length == 1);
        Assert.assertTrue(students[0].getName().equals("pippo"));
    }

    @Test
    public void loadTeacher() {
        Room room = new Room();
        room.setCode(567);
        roomId = roomService.create(room);

        Teacher[] teachers = roomService.loadTeacherByRoom(roomId);
        System.out.println(Arrays.toString(teachers));
        Assert.assertTrue(teachers == null || teachers.length == 0);

        Teacher teacher = new Teacher();
        teacher.setName("hippo");
        roomService.setTeacher(roomId, teacher);
        teachers = roomService.loadTeacherByRoom(roomId);
        System.out.println(Arrays.toString(teachers));
        Assert.assertTrue(teachers != null);
        Assert.assertTrue(teachers[0].getName().equals("hippo"));
    }

}
