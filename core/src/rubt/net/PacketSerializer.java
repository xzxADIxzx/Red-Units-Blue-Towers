package rubt.net;

import arc.net.FrameworkMessage;
import arc.net.FrameworkMessage.*;
import rubt.io.Reads;
import rubt.io.Writes;
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

        if (Net.provider != null) Net.provider.written(buffer.position());
    }

    @Override
    public Object read(ByteBuffer buffer) {
        if (Net.provider != null) Net.provider.readed(buffer.remaining());

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
        packet.write(Writes.of(buffer));
    }

    public Packet readPacket(ByteBuffer buffer) {
        Packet packet = Packets.newPacket(buffer.get());
        packet.read(Reads.of(buffer));

        return packet;
    }

    // endregion
}
