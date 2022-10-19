package rubt.net;

import arc.net.FrameworkMessage;
import arc.net.FrameworkMessage.*;
import arc.net.NetSerializer;

import java.nio.ByteBuffer;

public class PacketSerializer implements NetSerializer {

    @Override
    public void write(ByteBuffer buffer, Object object) {
        if (object instanceof FrameworkMessage message) {
            buffer.put((byte) 1);
            writeFramework(buffer, message);
        }
    }

    @Override
    public Object read(ByteBuffer buffer) {
        byte id = buffer.get();
        if (id == 1) return readFramework(buffer);
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
}
