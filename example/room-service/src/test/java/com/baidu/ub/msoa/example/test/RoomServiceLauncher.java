package com.baidu.ub.msoa.example.test;

import com.baidu.ub.msoa.container.BundleConstants;
import com.baidu.ub.msoa.container.ContainerLauncher;

/**
 * Created by pippo on 15/8/22.
 */
public class RoomServiceLauncher implements BundleConstants {

    public static void main(String[] args) {
        System.setProperty(MSOA_VM_OPTIONS_PROFILE, "test");
        ContainerLauncher.main(null);
    }

}
