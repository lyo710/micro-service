package com.baidu.ub.msoa.event;

/**
 * Created by pippo on 15/7/19.
 */
public interface EventHandlerContext {

    /**
     * fire next handler
     *
     * @param event
     * @throws Throwable
     */
    void passThrough(Event<?> event);

    /**
     * @return current handler
     */
    EventHandler getCurrentHandler();

    /**
     * @return interrupt handler
     */
    EventHandler getInterruptHandler();

    /**
     * @param key
     * @param <T>
     * @return attribute value with given key
     */
    <T> T getAttribute(Object key);

    /**
     * set attribute value with given key
     *
     * @param key
     * @param value
     */
    void setAttribute(String key, Object value);

    /**
     * remove attribute value with given key
     *
     * @param key
     */
    void removeAttribute(String key);

    /**
     * @param key
     * @return is attribute value exists with given key
     */
    boolean containsAttribute(String key);

    /**
     * destroy context
     */
    void destroy();
}
