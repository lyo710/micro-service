package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.RouteMeta;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by pippo on 15/7/20.
 */
public class Packet extends ProtobufCodec implements RouterConstants {

    public Packet() {

    }

    public Packet(String routeId) {
        this.routeId = routeId;
    }

    public Packet(byte[] meta, byte[] body) {
        this.routeId = UUID.randomUUID().toString();

        this.meta = meta;
        this.body = body;
    }

    public String routeId;
    public int metaLength;
    public int bodyLength;
    public byte[] meta;
    public byte[] body;

    public Packet from(ByteBuffer buffer) {
        routeId = getString(buffer, H_ROUTE_ID_SIZE);
        metaLength = buffer.getInt();
        bodyLength = buffer.getInt();
        meta = getBytes(buffer, metaLength);
        body = getBytes(buffer, bodyLength);
        return this;
    }

    public ByteBuffer to() {
        try {
            byte[] routeId = this.routeId.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + meta.length + body.length)
                    .put(routeId)
                    .putInt(meta.length)
                    .putInt(body.length)
                    .put(meta)
                    .put(body);

            buffer.flip();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public RouteMeta getRouteMeta() {
        try {
            Object[] oo = decode(meta, RouteMeta.class);
            return oo.length > 0 ? (RouteMeta) oo[0] : null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Object[] getArguments(Class<?>[] argumentTyps) {
        return decode(body, argumentTyps);
    }

    public Packet setRelust(Object... result) {
        body = encode(result);
        return this;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public int getMetaLength() {
        return metaLength;
    }

    public void setMetaLength(int metaLength) {
        this.metaLength = metaLength;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getMeta() {
        return meta;
    }

    public void setMeta(byte[] meta) {
        this.meta = meta;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBytes(ByteBuffer buffer, int length) {
        byte[] b = new byte[length];
        buffer.get(b);
        return b;
    }

    public String getString(ByteBuffer buffer, int length) {
        return new String(getBytes(buffer, length));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("routeId", routeId)
                .append("metaLength", metaLength)
                .append("bodyLength", bodyLength)
                .append("meta", meta)
                .append("body", body)
                .toString();
    }

    public static void main(String[] args) {
        Packet packet = new Packet(UUID.randomUUID().toString());
        System.out.println(packet.routeId);

        packet.meta = packet.encode(new RouteMeta(1, "bb", 1, "dd", RouteStrategy.local));
        packet.body = packet.encode("test1243");
        packet.metaLength = packet.meta.length;
        packet.bodyLength = packet.body.length;

        System.out.println(HEADER_SIZE + packet.meta.length + packet.body.length);

        ByteBuffer buffer = packet.to();
        buffer.flip();
        // ByteBuffer buffer = ByteBuffer.wrap(packet.to().array());

        System.out.println(packet.getString(buffer, H_ROUTE_ID_SIZE));

        System.out.println(buffer.getInt() + "#" + packet.metaLength);
        System.out.println(buffer.getInt() + "#" + packet.bodyLength);
        System.out.println(packet.decode(packet.getBytes(buffer, packet.metaLength), RouteMeta.class)[0]);
        System.out.println(packet.decode(packet.getBytes(buffer, packet.bodyLength), String.class)[0]);

        buffer.flip();
        packet = packet.from(buffer);
        System.out.println(packet);

    }

}
