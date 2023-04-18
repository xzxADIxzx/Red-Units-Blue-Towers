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
        else if (message instanceof DiscoverHost) buffer.put((byte) 4);
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
        buffer.put(Packets.packetId(packet));
        packet.write(new Writes(buffer));
    }

    public Packet readPacket(ByteBuffer buffer) {
        Packet packet = Packets.newPacket(buffer.get());
        packet.read(new Reads(buffer));

        return packet;
    }

    // endregion

    public static class Writes extends ByteBufferOutput {

        public Writes(ByteBuffer buffer) {
            super(buffer);
        }

        public void writeStr(String str) {
            try {
                writeBoolean(str == null);
                if (str != null) writeUTF(str);
            } catch (Exception ignored) {}
        }

        public void writePos(Position pos) {
            writeBoolean(pos == null);
            if (pos != null) buffer.putFloat(pos.getX()).putFloat(pos.getY());
        }
    }

    public static class Reads extends ByteBufferInput {

        public Reads(ByteBuffer buffer) {
            super(buffer);
        }

        public String readStr() {
            try {
                if (readBoolean()) return null;
                return readUTF();
            } catch (Exception ignored) {
                return null;
            }
        }

        public Position readPos() {
            if (readBoolean()) return null;
            return new Vec2(buffer.getFloat(), buffer.getFloat());
        }
    }
}
