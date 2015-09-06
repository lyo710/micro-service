package com.baidu.ub.msoa.governance.inbound.controller.console;

import com.baidu.ub.msoa.governance.domain.model.AppInfo;
import com.baidu.ub.msoa.governance.service.app.AppInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/31.
 */
@Controller
public class ProviderController {

    @Resource
    private AppInfoService appInfoService;

    @RequestMapping("console/provider")
    public String list(ModelMap context) {
        context.put("providers", appInfoService.loadAll());
        return "provider";
    }

    @RequestMapping("console/provider/persist")
    public String persist(AppInfo appInfo) {
        appInfoService.persist(appInfo);
        return "redirect:/service-governance/console/index.html?tab=provider";
    }

    @RequestMapping("console/provider/delete/{appId}")
    public String delete(@PathVariable("appId") int appId) {
        appInfoService.delete(appId);
        return "redirect:/service-governance/console/index.html?tab=provider";
    }
}
