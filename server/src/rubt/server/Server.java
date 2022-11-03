package rubt.server;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import rubt.Groups;
import rubt.net.*;
import rubt.net.Packet.*;
import rubt.world.Unit;

public class Server extends arc.net.Server implements NetListener {

    public PacketHandler handler = new PacketHandler();

    public Server() {
        super(32768, 8192, new PacketSerializer());
        addListener(this);

        handler.register(UnitCreate.class, (con, create) -> {
            create.execute();
            Send.createUnit(Groups.units.peek());
        });

        handler.register(UnitUpdate.class, (con, update) -> {
            Unit unit = Groups.units.get(update.unitID);
            unit.target = update.target;
        });

        handler.register(TurretCreate.class, (con, create) -> {
            create.execute();
            Send.createTurret(Groups.turrets.peek());
        });
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

        Log.info("@ received.", connection);
    }

    public void disconnected(Connection connection, DcReason reason) {
        Groups.connections.remove(connection);

        Log.info("@ disconnected: @.", connection, reason);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) try {
            handler.handle(connection, packet);
        } catch (Throwable ignored) {
            Log.err("Unable to process client packet", ignored);
        }
    }

    public void idle(Connection connection) {}

    // endregion
}
