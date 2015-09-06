package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.baidu.ub.msoa.container.support.rpc.RPCConstants;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCResponse implements RPCConstants {

    public RPCResponse() {

    }

    public RPCResponse(long id, int status, RPCResult result) {
        this.id = id;
        this.status = status;
        this.result = result;
    }

    //    @Override
    //    public byte[] encode(Encoder encoder) {
    //        byte[] rpcResultBytes = encoder.encode(result);
    //        length = 8 + 4 + rpcResultBytes.length;
    //        ByteBuffer output = ByteBuffer.allocate(4 + length);
    //        output.putInt(length);
    //        output.putLong(id);
    //        output.putInt(status);
    //        output.putInt(rpcResultBytes.length);
    //        output.put(rpcResultBytes);
    //        return output.array();
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    @Override
    //    public RPCResponse decode(Decoder decoder, byte[] message) {
    //        ByteBuffer input = ByteBuffer.wrap(message);
    //        length = input.getInt();
    //        id = input.getLong();
    //        status = input.getInt();
    //        byte[] rpcResultBytes = new byte[input.getInt()];
    //        input.get(rpcResultBytes);
    //        decoder.decode(rpcResultBytes, RPCResult.class);
    //        return this;
    //    }

    //    public int length;
    public long id;
    public int status;
    public RPCResult result;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RPCResult getResult() {
        return result;
    }

    public void setResult(RPCResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("RPCResponse{'id'=%s, 'status'=%s, 'result'=%s}", id, status, result);
    }
}
