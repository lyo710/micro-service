package com.baidu.ub.msoa.container.support.governance.domain.dto.spring;

import org.springframework.context.ApplicationEvent;

/**
 * Created by pippo on 15/8/22.
 */
public class ServiceRegisterAcceptEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ServiceRegisterAcceptEvent(Object source) {
        super(source);
    }

}
