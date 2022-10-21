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

    public void sendWorldData(Connection connection) {
        Groups.tiles.each(tile -> Send.createTile(connection, tile));
        Groups.units.each(unit -> Send.createUnit(connection, unit));
        Groups.turrets.each(turret -> Send.createTurret(connection, turret));
    }

    // region listeners

    public void connected(Connection connection) {
        Groups.connections.add(connection);
        sendWorldData(connection);
    }

    public void disconnected(Connection connection, DcReason reason) {
        Groups.connections.remove(connection);
    }

    public void received(Connection connection, Object object) {}

    public void idle(Connection connection) {}

    // endregion
}
