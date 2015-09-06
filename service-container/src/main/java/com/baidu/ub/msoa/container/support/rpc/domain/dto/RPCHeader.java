package com.baidu.ub.msoa.container.support.rpc.domain.dto;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCHeader {

    //    @Override
    //    public byte[] encode(Encoder encoder) {
    //        return encoder.encode(this);
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    @Override
    //    public RPCHeader decode(Decoder decoder, byte[] message) {
    //        return decoder.decode(message, RPCHeader.class);
    //    }

    public RPCDestination destination;
    public RPCTrace trace = new RPCTrace();

    public RPCDestination getDestination() {
        return destination;
    }

    public void setDestination(RPCDestination destination) {
        this.destination = destination;
    }

    public RPCTrace getTrace() {
        return trace;
    }

    public void setTrace(RPCTrace trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        return String.format("RPCHeader{''destination'=%s, 'trace'=%s}", destination, trace);
    }
}
