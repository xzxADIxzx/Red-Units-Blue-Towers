package rubt.net;

import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.net.FrameworkMessage;
import arc.net.FrameworkMessage.*;
import rubt.net.Packet.*;
import arc.net.NetSerializer;

import java.nio.ByteBuffer;

public class PacketSerializer implements NetSerializer {

    @Override
    public void write(ByteBuffer buffer, Object object) {
        if (object instanceof FrameworkMessage message) {
            buffer.put((byte) 1);
            writeFramework(buffer, message);
        } else if (object instanceof Packet packet) {
            buffer.put((byte) 2);
            writePacket(buffer, packet);
        }
    }

    @Override
    public Object read(ByteBuffer buffer) {
        byte id = buffer.get();
        if (id == 1) return readFramework(buffer);
        if (id == 2) return readPacket(buffer);
        throw new RuntimeException("Unknown message!"); // what?
    }

    public void writeFramework(ByteBuffer buffer, FrameworkMessage message) {
        if (message instanceof Ping ping) buffer.put((byte) 1).putInt(ping.id).put(ping.isReply ? (byte) 1 : 0);
        else if (message instanceof RegisterTCP reg) buffer.put((byte) 2).putInt(reg.connectionID);
        else if (message instanceof RegisterUDP reg) buffer.put((byte) 3).putInt(reg.connectionID);
        else if (message instanceof DiscoverHost) buffer.put((byte)4);
        else if (message instanceof KeepAlive) buffer.put((byte) 5);
    }

    public FrameworkMessage readFramework(ByteBuffer buffer) {
        byte id = buffer.get();
        if (id == 1)
            return new Ping() {{
                id = buffer.getInt();
                isReply = buffer.get() == 1;
            }};
        else if (id == 2)
            return new RegisterTCP() {{
                connectionID = buffer.getInt();
            }};
        else if (id == 3)
            return new RegisterUDP() {{
                connectionID = buffer.getInt();
            }};
        else if (id == 3) return FrameworkMessage.discoverHost;
        else if (id == 4) return FrameworkMessage.keepAlive;
        throw new RuntimeException("Unknown framework message!"); // how is that even possible?
    }

    public void writePacket(ByteBuffer buffer, Packet packet) {
        if (packet instanceof UnitUpdate update) {
            buffer.put((byte) 1).putInt(update.id).putInt(update.unitID);
            buffer.putFloat(update.position.x).putFloat(update.position.y);
            buffer.putFloat(update.target.getX()).putFloat(update.target.getY());
        } else if (packet instanceof TurretUpdate update) {
            buffer.put((byte) 2).putInt(update.id).putInt(update.turretID);
            buffer.putInt(update.position.pack()).putFloat(update.angel);
        }
    }

    public Packet readPacket(ByteBuffer buffer) {
        byte id = buffer.get();
        if (id == 1)
            return new UnitUpdate() {{
                id = buffer.getInt();
                unitID = buffer.getInt();

                position = new Vec2(buffer.getFloat(), buffer.getFloat());
                target = new Vec2(buffer.getFloat(), buffer.getFloat());
            }};
        if (id == 2)
            return new TurretUpdate() {{
                id = buffer.getInt();
                turretID = buffer.getInt();

                position = Point2.unpack(buffer.getInt());
                angel = buffer.getFloat();
            }};
        throw new RuntimeException("Unknown packet!");
    }

    public static void writeString(ByteBuffer buffer, String message) {
        if (message == null) message = "null";
        buffer.putInt(message.length());
        for (char chara : message.toCharArray())
            buffer.putChar(chara);
    }

    public static String readString(ByteBuffer buffer) {
        int length = buffer.getInt();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
            builder.append(buffer.getChar());
        return builder.toString();
    }
}
