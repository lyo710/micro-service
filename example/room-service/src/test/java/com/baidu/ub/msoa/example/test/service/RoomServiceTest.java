package com.baidu.ub.msoa.example.test.service;

import com.baidu.ub.msoa.container.test.BundleIntegrationTest;
import com.baidu.ub.msoa.example.room.domain.model.Room;
import com.baidu.ub.msoa.example.room.service.RoomService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/20.
 */
@BundleIntegrationTest(bundle = "room-service")
@RunWith(SpringJUnit4ClassRunner.class)
public class RoomServiceTest {

    @Resource
    private RoomService roomService;

    @Test
    public void createRoom() {
        Room room = new Room();
        room.setCode(123);
        long roomId = roomService.create(room);
        System.out.println(roomId);
        Assert.assertTrue(roomId > 0);
    }

}
