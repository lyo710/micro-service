package com.baidu.ub.msoa.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pippo on 15/7/19.
 */
public class DefaultEventHandlerContext implements EventHandlerContext {

    private static Logger logger = LoggerFactory.getLogger(DefaultEventHandlerContext.class);

    public DefaultEventHandlerContext(EventHandlerStack stack) {
        this.stack = stack;
        this.current = stack.top;
        this.attributes = new HashMap<>();
    }

    protected EventHandlerStack stack;
    protected EventHandlerElement current;
    protected EventHandlerElement interrupt;

    protected Map<String, Object> attributes;

    /* true=downstream,false=upstream */
    protected boolean streamFlag = true;

    @Override
    public void passThrough(final Event<?> event) {

        if (current == null) {
            return;
        }

        if (streamFlag) {
            doPassThrough(event);
        }

    }

    @SuppressWarnings("unchecked")
    protected void doPassThrough(Event event) {
        EventHandlerElement tmpCurrent = current;

        try {
            if (tmpCurrent != null) {
                current = tmpCurrent.next;
                down(tmpCurrent, event);
            }

            streamFlag = false;
            current = tmpCurrent;
            up(current, event);
        } catch (Throwable throwable) {
            if (tmpCurrent != null) {
                tmpCurrent.onException(this, event, throwable);
            }

            if (interrupt == null) {
                interrupt = tmpCurrent;
            }

            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void down(EventHandlerElement handler, Event event) throws Throwable {

        if (handler.accept(event)) {

            if (logger.isTraceEnabled()) {
                logger.trace("[{}] downstream on event:[{}]", handler.getName(), event);
            }

            handler.downstream(this, event);
        } else {
            passThrough(event);
        }

    }

    @SuppressWarnings("unchecked")
    protected void up(EventHandlerElement handler, Event event) throws Throwable {

        if (handler.accept(event)) {

            if (logger.isTraceEnabled()) {
                logger.trace("[{}] upstream on event:[{}]", handler.getName(), event);
            }

            handler.upstream(this, event);
        }

    }

    @Override
    public EventHandler getCurrentHandler() {
        return current != null ? current.delegate : null;
    }

    @Override
    public EventHandler getInterruptHandler() {
        return interrupt != null ? interrupt.delegate : current.delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(Object key) {
        return (T) attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public boolean containsAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public void destroy() {
        if (attributes != null) {
            attributes.clear();
        }

        stack = null;
        current = null;
        attributes = null;
    }

}
