package com.baidu.ub.msoa.governance.service.app.impl;

import com.baidu.ub.msoa.governance.domain.model.AppInfo;
import com.baidu.ub.msoa.governance.domain.repository.AppInfoRepository;
import com.baidu.ub.msoa.governance.service.app.AppInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pippo on 15/8/31.
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {

    @Resource
    private AppInfoRepository appInfoRepository;

    @Override
    public AppInfo get(int id) {
        AppInfo info = appInfoRepository.get(id);

        if (info == null) {
            info = new AppInfo();
            info.setId(id);
            info.setName("provider=" + id);
            persist(info);
        }

        return info;
    }

    @Override
    public void persist(AppInfo appInfo) {
        appInfoRepository.persist(appInfo);
    }

    @Override
    public List<AppInfo> loadAll() {
        return appInfoRepository.loadAll();
    }

    @Override
    public void delete(int appId) {
        appInfoRepository.delete(appId);
    }
}
