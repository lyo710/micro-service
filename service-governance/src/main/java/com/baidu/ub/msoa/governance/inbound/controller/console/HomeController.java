package com.baidu.ub.msoa.governance.inbound.controller.console;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by pippo on 15/8/28.
 */
@Controller
public class HomeController {

    @RequestMapping("console/index.html")
    public String index(@RequestParam(value = "tab", required = false, defaultValue = "topology") String tab,
            ModelMap context) {
        context.put("tab", tab);
        return "index";
    }

}
