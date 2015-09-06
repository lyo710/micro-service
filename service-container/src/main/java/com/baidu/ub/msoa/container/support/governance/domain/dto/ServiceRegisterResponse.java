package com.baidu.ub.msoa.container.support.governance.domain.dto;

import com.baidu.ub.msoa.container.support.governance.domain.model.registry.RegisterInfo;

import java.util.Arrays;

/**
 * Created by pippo on 15/8/3.
 */
public class ServiceRegisterResponse extends BaseResponse {

    private RegisterInfo[] infos;

    public RegisterInfo[] getInfos() {
        return infos;
    }

    public void setInfos(RegisterInfo[] infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return String.format("ServiceRegisterResponse{'super'=%s, 'success'=%s, 'error'=%s, 'infos'=%s}",
                super.toString(),
                success,
                error,
                Arrays.toString(infos));
    }
}
