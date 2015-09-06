package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/19.
 */
public interface Event<T> {

    /**
     * getDefault event type
     *
     * @return
     */
    int getType();

    /**
     * @return event payload
     */
    T getPayload();

    /**
     * set event payload
     *
     * @param t
     */
    void setPayload(T t);

}
