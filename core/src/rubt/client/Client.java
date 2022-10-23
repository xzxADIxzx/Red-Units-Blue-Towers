package rubt.client;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import rubt.net.Packet;
import rubt.net.PacketSerializer;

public class Client extends arc.net.Client implements NetListener {

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);
    }

    // region listeners

    public void connected(Connection connection) {}

    public void disconnected(Connection connection, DcReason reason) {}

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) packet.execute();
    }

    public void idle(Connection connection) {}

    // endregion
}