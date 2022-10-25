package rubt.client;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import rubt.net.Packet;
import rubt.net.PacketSerializer;

public class Client extends arc.net.Client implements NetListener {

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);
    }

    // region listeners

    public void connected(Connection connection) {
        var tcp = connection.getRemoteAddressTCP();
        Log.info("Connecting to @:@", tcp.getHostName(), tcp.getPort());
    }

    public void disconnected(Connection connection, DcReason reason) {
        Log.info("Connection closed: @.", reason);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) packet.execute();
    }

    public void idle(Connection connection) {}

    // endregion
}