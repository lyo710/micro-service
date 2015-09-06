package com.baidu.ub.msoa.utils.proto;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/7/19.
 */
public class ProtobufCodec {

    private static final byte[] BYTES = new byte[0];
    private static final Object[] OBJECTS = new Object[0];

    /**
     * encode object to byte[]
     *
     * @param arguments
     * @return byte[]
     */
    public byte[] encode(Object... arguments) {
        if (arguments == null || arguments.length == 0) {
            return BYTES;
        }

        ByteArrayDataOutput buffer = ByteStreams.newDataOutput();
        buffer.writeShort(arguments.length);

        for (Object argument : arguments) {
            byte[] arg = ProtostuffReflect.encode(argument);
            buffer.writeInt(arg.length);
            buffer.write(arg);
        }

        return buffer.toByteArray();
    }

    /**
     * decode byte[] to object[]
     *
     * @param body
     * @param types
     * @return object[]
     */
    public Object[] decode(byte[] body, Class<?>... types) {
        if (body == null || body.length == 0) {
            return OBJECTS;
        }

        ByteArrayDataInput buffer = ByteStreams.newDataInput(body);
        int size = buffer.readShort();

        if (size != types.length) {
            throw new IllegalArgumentException(String.format(
                    "need parameters size:[%s] not match input arguments size:[%s]",
                    types.length,
                    size));
        }

        List<Object> results = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            byte[] b = new byte[buffer.readInt()];
            buffer.readFully(b);
            results.add(ProtostuffReflect.decode(b, types[i]));
        }

        return results.toArray();
    }

}
