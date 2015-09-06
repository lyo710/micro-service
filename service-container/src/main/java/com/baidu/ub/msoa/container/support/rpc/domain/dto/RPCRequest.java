package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.baidu.ub.msoa.container.support.rpc.RPCConstants;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCRequest implements RPCConstants {

    /* 只需要保证在每个process的运行期内唯一即可,主要用于异步io消息处理 */
    public static final AtomicLong REQUEST_ID = new AtomicLong(0);

    //    @Override
    //    public byte[] encode(Encoder encoder) {
    //        byte[] headerBytes = header.encode(encoder);
    //        byte[] argumentBytes = arguments.encode(encoder);
    //        length = 8 + 4 + headerBytes.length + 4 + argumentBytes.length;
    //
    //        ByteBuffer output = ByteBuffer.allocate(4 + length);
    //        /* total length */
    //        output.putInt(length);
    //        /* request id */
    //        output.putLong(id);
    //        /* header length */
    //        output.putInt(headerBytes.length);
    //        /* header bytes */
    //        output.put(headerBytes);
    //        /* argument length */
    //        output.putInt(argumentBytes.length);
    //        /* argument bytes */
    //        output.put(argumentBytes);
    //
    //        return output.array();
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    @Override
    //    public RPCRequest decode(Decoder decoder, byte[] message) {
    //        ByteBuffer input = ByteBuffer.wrap(message);
    //        length = input.getInt();
    //        id = input.getLong();
    //
    //        byte[] headerBytes = new byte[input.getInt()];
    //        input.get(headerBytes);
    //        header = new RPCHeader().decode(decoder, headerBytes);
    //
    //        byte[] argumentBytes = new byte[input.getInt()];
    //        input.get(argumentBytes);
    //        arguments = new RPCArguments().decode(decoder, argumentBytes);
    //
    //        return this;
    //    }

    //    public int length;
    public long id = REQUEST_ID.incrementAndGet();
    public RPCHeader header = new RPCHeader();
    public RPCArguments arguments = new RPCArguments();

    //    public int getLength() {
    //        return length;
    //    }
    //
    //    public void setLength(int length) {
    //        this.length = length;
    //    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RPCHeader getHeader() {
        return header;
    }

    public void setHeader(RPCHeader header) {
        this.header = header;
    }

    public RPCArguments getArguments() {
        return arguments;
    }

    public void setArguments(RPCArguments arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return String.format("RPCRequest{'id'=%s, 'header'=%s, 'arguments'=%s}", id, header, arguments);
    }
}
