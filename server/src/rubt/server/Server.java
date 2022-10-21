package rubt.server;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import rubt.Groups;
import rubt.net.PacketSerializer;
import rubt.net.Send;

public class Server extends arc.net.Server implements NetListener {

    public Server() {
        super(32768, 8192, new PacketSerializer());
        addListener(this);
    }

    public void sync() {
        Groups.units.each(Send::updateUnit);
        Groups.turrets.each(Send::updateTurret);
    }

    // region listeners

    public void connected(Connection connection) {}

    public void disconnected(Connection connection, DcReason reason) {}

    public void received(Connection connection, Object object) {}

    public void idle(Connection connection) {}

    // endregion
}
