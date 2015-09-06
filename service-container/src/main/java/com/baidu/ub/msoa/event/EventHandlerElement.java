package com.baidu.ub.msoa.event;

import com.google.common.base.Verify;

/**
 * Created by pippo on 15/7/19.
 */
public class EventHandlerElement<E extends Event<?>> implements EventHandler<E> {

    public EventHandlerElement(EventHandler delegate) {
        Verify.verifyNotNull(delegate);
        this.delegate = delegate;
    }

    protected EventHandler delegate;
    protected EventHandlerElement previous;
    protected EventHandlerElement next;

    @Override
    public String getName() {
        return delegate.getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean accept(Event<?> event) {
        return delegate.accept(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void downstream(EventHandlerContext context, E event) throws Throwable {
        delegate.downstream(context, event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void upstream(EventHandlerContext context, E event) throws Throwable {
        delegate.upstream(context, event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onException(EventHandlerContext context, E event, Throwable throwable) {
        delegate.onException(context, event, throwable);
    }

    @Override
    public String toString() {
        return String.format("{%s->[%s]->%s}",
                previous != null ? previous.delegate.getName() : "",
                delegate.getName(),
                next != null ? next.delegate.getName() : "");
    }

}
