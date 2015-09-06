package com.baidu.ub.msoa.governance.service.app;

import com.baidu.ub.msoa.container.support.governance.contact.proto.annotaion.CollectionElementType;
import com.baidu.ub.msoa.container.support.governance.discover.annotation.BundleService;
import com.baidu.ub.msoa.governance.domain.model.AppInfo;

import java.util.List;

/**
 * Created by pippo on 15/8/31.
 */
@BundleService(name = "appInfoService", version = 1, interfaceType = AppInfoService.class)
public interface AppInfoService {

    AppInfo get(int id);

    void persist(AppInfo appInfo);

    // 由于java泛型的可擦拭特性,无法在运行期获得集合元素类型
    // 如需返回集合类型,需要显示声明内部元素类型
    @CollectionElementType(AppInfo.class)
    List<AppInfo> loadAll();

    void delete(int appId);
}
