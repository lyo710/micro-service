package com.baidu.ub.msoa.container.support.router.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pippo on 15/7/20.
 */
public class InputEvent extends RPCEvent {

    public InputEvent(EventType type, byte[] input) {
        this.type = type;
        this.input = input;
    }

    public InputEvent(EventType type, RouteMessage message, byte[] input) {
        this.type = type;
        this.message = message;
        this.input = input;
    }

    public byte[] input;
    public byte[] output;

    public byte[] getInput() {
        return input;
    }

    public void setInput(byte[] input) {
        this.input = input;
    }

    public byte[] getOutput() {
        return output;
    }

    public void setOutput(byte[] output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("type", type)
                .append("message", message)
                .toString();
    }
}
