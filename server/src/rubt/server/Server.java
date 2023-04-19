package rubt.server;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import rubt.Groups;
import rubt.logic.*;
import rubt.net.*;
import rubt.net.PacketSerializer.Writes;
import rubt.net.Packets.*;

import static rubt.Vars.*;

import java.nio.ByteBuffer;

public class Server extends arc.net.Server implements NetListener {

    public PacketHandler handler = new PacketHandler();

    /** Output for writing snapshots. */
    public Writes sync = new Writes(ByteBuffer.allocate(snapshotSize));

    public Server() {
        super(32768, 8192, new PacketSerializer());
        addListener(this);

        setMulticast(multicast, multicastPort);
        setDiscoveryHandler((address, handler) -> {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            Host.write(new Writes(buffer));

            buffer.position(0);
            handler.respond(buffer);

            Log.info("Server was discovered by the client with the address @", address.getHostName());
        });

        handler.register(PlayerCreate.class, (con, data) -> {
            if (rules.strict && Groups.players.contains(player -> con.getRemoteAddressTCP().getAddress().getHostName().equals(player.ip()))) return;

            Player player = new Player(con);

            player.avatar = data.avatar;
            player.name = data.name;

            player.team = Logic.nextTeam();
            player.admin = false; // TODO admin system?

            Send.createPlayer(player);
        });

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

    public void sendSnapshot() {
        sync.buffer.clear();
        byte sent = 0;

        for (var object : Groups.sync) {
            sent++;

            sync.writeInt(object.netId);
            object.write(sync);

            if (sync.buffer.position() > snapshotSize || sent >= 255) {
                Send.snapshot(sent, sync.buffer.array());

                sync.buffer.clear();
                sent = 0;
            }
        }

        if (sent > 0) Send.snapshot(sent, sync.buffer.array());
    }

    public void sendWorldData(Connection connection) {
        Groups.tiles.each(tile -> Send.createTile(connection, tile));
        Groups.units.each(unit -> Send.createUnit(connection, unit));
        Groups.turrets.each(turret -> Send.createTurret(connection, turret));
        Groups.players.each(player -> Send.createPlayer(connection, player));
        Send.updateState(connection);
    }

    // region listeners

    public void connected(Connection connection) {
        Groups.connections.add(connection);
        sendWorldData(connection);

        Log.info("@ received.", connection);
    }

    public void disconnected(Connection connection, DcReason reason) {
        Groups.connections.remove(connection);
        // TODO mark player as disconnected and clear on game reset

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
