package rubt.net;

import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.net.FrameworkMessage;
import arc.net.FrameworkMessage.*;
import arc.util.io.ByteBufferInput;
import arc.util.io.ByteBufferOutput;
import rubt.net.Packets.*;
import arc.net.NetSerializer;

import java.nio.ByteBuffer;

public class PacketSerializer implements NetSerializer {

    // region general

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

    // endregion
    // region framework

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
        else if (id == 4) return FrameworkMessage.discoverHost;
        else if (id == 5) return FrameworkMessage.keepAlive;
        throw new RuntimeException("Unknown framework message!"); // how is that even possible?
    }

    // endregion
    // region packet

    public void writePacket(ByteBuffer buffer, Packet packet) {
        if (packet instanceof StateUpdate update) {
            buffer.put((byte) 0).putInt(update.id);
        } else if (packet instanceof PlayerCreate data) {
            buffer.put((byte) 1);
            writeString(buffer, data.name);
            buffer.putInt(data.team).put(data.admin ? (byte) 1 : 0);
        } else if (packet instanceof TileCreate create) {
            buffer.put((byte) 3);
            buffer.putInt(create.x).putInt(create.y);
        } else if (packet instanceof TileUpdate update) {
            buffer.put((byte) 4);
        } else if (packet instanceof UnitCreate create) {
            buffer.put((byte) 5).putInt(create.type);
            writeVector(buffer, create.position);
        } else if (packet instanceof UnitUpdate update) {
            buffer.put((byte) 6).putInt(update.unitID);
            writeVector(buffer, update.position);
            writeVector(buffer, update.target);
            buffer.putFloat(update.rotation);
        } else if (packet instanceof TurretCreate create) {
            buffer.put((byte) 7).putInt(create.type);
            writeVector(buffer, create.position);
        } else if (packet instanceof TurretUpdate update) {
            buffer.put((byte) 8).putInt(update.turretID);
            buffer.putFloat(update.rotation);
        }
    }

    public Packet readPacket(ByteBuffer buffer) {
        byte id = buffer.get();
        if (id == 0)
            return new StateUpdate() {{
                id = buffer.getInt();
            }};
        else if (id == 1)
            return new PlayerCreate() {{
                avatar = null;
                name = readString(buffer);

                team = buffer.getInt();
                admin = buffer.get() == 1;
            }};
        else if (id == 3)
            return new TileCreate() {{
                x = buffer.getInt();
                y = buffer.getInt();
            }};
        else if (id == 4)
            return new TileUpdate();
        else if (id == 5)
            return new UnitCreate() {{
                type = buffer.getInt();
                position = readVector(buffer);
            }};
        else if (id == 6)
            return new UnitUpdate() {{
                unitID = buffer.getInt();

                position = readVector(buffer);
                target = readVector(buffer);
                rotation = buffer.getFloat();
            }};
        else if (id == 7)
            return new TurretCreate() {{
                type = buffer.getInt();
                position = readVector(buffer);
            }};
        else if (id == 8)
            return new TurretUpdate() {{
                turretID = buffer.getInt();

                rotation = buffer.getFloat();
            }};
        throw new RuntimeException("Unknown packet!");
    }

    // endregion
    // region string

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

    // endregion
    // region vector

    public static void writeVector(ByteBuffer buffer, Position position) {
        if (position == null)
            buffer.put((byte) 1);
        else {
            buffer.put((byte) 0);
            buffer.putFloat(position.getX()).putFloat(position.getY());
        }
    }

    public static Vec2 readVector(ByteBuffer buffer) {
        if (buffer.get() == 1) return null;
        return new Vec2(buffer.getFloat(), buffer.getFloat());
    }

    // endregion

    public static class Writes extends ByteBufferOutput {

        public Writes(ByteBuffer buffer) {
            super(buffer);
        }

        public void write(Position position) {
            writeBoolean(position == null);
            if (position != null) buffer.putFloat(position.getX()).putFloat(position.getY());
        }
    }

    public static class Reads extends ByteBufferInput {

        public Reads(ByteBuffer buffer) {
            super(buffer);
        }

        public Position read(Position position) {
            if (readBoolean()) return null;
            return new Vec2(buffer.getFloat(), buffer.getFloat());
        }
    }
}
