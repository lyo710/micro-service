package com.baidu.ub.msoa.container.test.proto;

import com.baidu.ub.msoa.utils.proto.ProtostuffReflect;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pippo on 15/8/24.
 */
public class ProtostuffTest {

    @Test
    public void testLong() throws IOException {
        Long l = 2L;
        byte[] bb = ProtostuffReflect.encode(l);

        Long ll = ProtostuffReflect.decode(bb, Long.class);
        System.out.println(ll);
        Assert.assertTrue(l.longValue() == ll.longValue());
    }

    @Test
    public void testLongNull() throws IOException {
        Long l = null;
        byte[] bb = ProtostuffReflect.encode(l);

        Long ll = ProtostuffReflect.decode(bb, Long.class);
        System.out.println(ll);
        Assert.assertTrue(ll == null);
    }

    @Test
    public void testCollection() throws IOException {

        List<Long> l = new ArrayList<>();
        l.add(1L);
        l.add(2L);

        Schema<Long> schema = RuntimeSchema.createFrom(Long.class);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtobufIOUtil.writeListTo(out, l, schema, LinkedBuffer.allocate(512));

        List<Long> ll = ProtobufIOUtil.parseListFrom(new ByteArrayInputStream(out.toByteArray()), schema);

        System.out.println(ll);
        Assert.assertTrue(ll.size() == 2);
        Assert.assertTrue(ll.get(0).intValue() == 1);

    }

    @Test
    public void testArray() throws IOException {

        Long[] l = new Long[] { 1L, 2L };

        byte[] data = ProtostuffReflect.encode(l);
        Long[] ll = ProtostuffReflect.decode(data, Long[].class);

        System.out.println(Arrays.toString(ll));
        Assert.assertTrue(ll.length == 2);
        Assert.assertTrue(ll[0].intValue() == 1);

    }

    @Test
    public void testArray1() throws IOException {

        User[] u = new User[] { new User("pippo"), new User("hippo") };

        byte[] data = ProtostuffReflect.encode(u);
        User[] uu = ProtostuffReflect.decode(data, User[].class);

        System.out.println(Arrays.toString(uu));
        Assert.assertTrue(uu.length == 2);
        Assert.assertTrue(uu[0].name.equals("pippo"));

    }

    private static class User {

        public User() {
        }

        public User(String name) {
            this.name = name;
        }

        private String name;

    }
}
