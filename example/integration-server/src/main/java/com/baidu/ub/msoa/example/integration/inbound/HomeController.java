package com.baidu.ub.msoa.example.integration.inbound;

import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.example.room.service.p_1.v_20150901.RoomService;
import org.springframework.stereotype.Controller;

/**
 * Created by pippo on 15/8/20.
 */
@Controller
public class HomeController {

    @BundleService
    private RoomService roomService;

    public void welcom() {

    }
}
