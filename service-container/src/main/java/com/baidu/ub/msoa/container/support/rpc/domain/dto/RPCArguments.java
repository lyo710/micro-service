package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.baidu.ub.msoa.container.support.rpc.RPCConstants;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.Codec;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.codec.CodecFactory.CodecType;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pippo on 15/9/2.
 */
public class RPCArguments implements RPCConstants {

    public RPCArguments() {

    }

    public RPCArguments(Object... args) {
        this.args = args;
    }

    public RPCArguments(CodecType codecType, Object... args) {
        this.codecType = codecType.code;
        this.args = args;
        this.args2bytes();
    }

    public RPCArguments args2bytes() {
        if (args == null || args.length == 0) {
            this.payload = BYTES;
            return this;
        }

        Codec codec = CodecFactory.getCodec(codecType);
        ByteArrayDataOutput buffer = ByteStreams.newDataOutput();
        buffer.writeShort(args.length);

        for (Object argument : args) {
            byte[] arg = codec.encode(argument);
            buffer.writeInt(arg.length);
            buffer.write(arg);
        }

        this.payload = buffer.toByteArray();
        return this;
    }

    public RPCArguments args2bytes(Object... args) {
        this.args = args;
        args2bytes();
        return this;
    }

    public Object[] bytes2args(Class<?>... types) {
        if (payload == null || payload.length == 0) {
            return OBJECTS;
        }

        Codec codec = CodecFactory.getCodec(codecType);

        ByteArrayDataInput buffer = ByteStreams.newDataInput(payload);
        int size = buffer.readShort();

        if (size != types.length) {
            throw new IllegalArgumentException(String.format(
                    "need parameters size:[%s] not match input payload size:[%s]",
                    types.length,
                    size));
        }

        List<Object> results = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            byte[] b = new byte[buffer.readInt()];
            buffer.readFully(b);
            results.add(codec.decode(b, types[i]));
        }

        this.args = results.toArray();
        return this.args;
    }

    public int codecType;
    public Object[] args;
    public byte[] payload;

    @Override
    public String toString() {
        return String.format("RPCArguments{'codecType'=%s, 'args'=%s, 'payload'=%s}",
                codecType,
                Arrays.toString(args),
                Arrays.toString(payload));
    }
}
