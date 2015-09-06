package com.baidu.ub.msoa.example.test;

import com.baidu.ub.msoa.container.support.governance.contact.proto.ServiceStubJarGeneratorTool;
import com.baidu.ub.msoa.example.room.service.RoomService;

/**
 * Created by pippo on 15/8/20.
 */
public class JarGenerator {

    public static void main(String[] args) throws Exception {
        ServiceStubJarGeneratorTool.generate(1, RoomService.class, JarGenerator.class.getResource("/").getFile());
    }

}
