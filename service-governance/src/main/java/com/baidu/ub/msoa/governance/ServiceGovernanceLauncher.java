package com.baidu.ub.msoa.governance;

import com.baidu.ub.msoa.container.BundleConstants;
import com.baidu.ub.msoa.container.ContainerLauncher;

/**
 * Created by pippo on 15/8/23.
 */
public class ServiceGovernanceLauncher implements BundleConstants {

    public static void main(String[] args) {
        // System.setProperty(MSOA_VM_OPTIONS_PROFILE, "test");
        ContainerLauncher.main(new String[] { "0.0.0.0", "8156" });
    }

}
