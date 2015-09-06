package com.baidu.ub.msoa.container.support.governance.domain.model.registry;

import com.baidu.ub.msoa.container.support.governance.contact.ContactConflictException;

import java.io.File;

/**
 * Created by pippo on 15/8/3.
 */
public interface ServiceContact {

    /**
     * 判断契约是否冲突(遵循Consumer Contract Driven的原则)
     *
     * @param contact
     * @return 是否冲突
     */
    void isConflict(ServiceContact contact) throws ContactConflictException;

    /**
     * @return text schema
     */
    String getSchema();

    /**
     * @return 根据契约生成的service stub sdk
     * @throws Exception
     */
    File stubJar() throws Exception;
}
