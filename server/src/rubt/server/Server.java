package rubt.server;

import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import rubt.Groups;
import rubt.Groups.Entity;
import rubt.io.Writes;
import rubt.logic.*;
import rubt.net.*;
import rubt.net.Packets.*;
import rubt.world.Turret;
import rubt.world.Unit;

import static rubt.Vars.*;

import java.io.*;
import java.nio.ByteBuffer;

public class Server extends arc.net.Server implements NetListener {

    public PacketHandler handler = new PacketHandler();

    /** Output for writing snapshots. */
    public ByteBuffer sync = ByteBuffer.allocate(8192);

    public Server() {
        super(32768, 8192, new PacketSerializer());
        addListener(this);

        setMulticast(multicast, multicastPort);
        setDiscoveryHandler((address, handler) -> {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            Host.write(Writes.of(buffer));

            buffer.position(0);
            handler.respond(buffer);

            Log.info("Server was discovered by the client with the address @", address.getHostName());
        });

        handler.register(PlayerData.class, (con, data) -> {
            if (rules.strict && Groups.players.contains(player -> con.getRemoteAddressTCP().getAddress().getHostName().equals(player.ip()))) return;

            String name = data.name.trim();
            if (name.length() > maxNameLength) name = name.substring(0, maxNameLength);
            if (name.isBlank()) name = "Nooby";

            Player player = new Player();

            player.con = con;
            player.avatar = data.avatar;
            player.name = name;
            player.team = Logic.nextTeam();
            player.admin = false; // TODO admin system?

            sendEntity(player);
        });

        handler.register(SpawnUnit.class, (con, data) -> {
            // TODO check player's team, unit's cost and spawn position

            Unit unit = new Unit();
            unit.type = data.type;
            unit.set(data.position);
            unit.target = data.position;

            sendEntity(unit);
        });

        handler.register(BuildTurret.class, (con, data) -> {
            // TODO check player's team, turret's cost and tile

            Turret turret = new Turret();
            turret.type = data.type;
            turret.set(data.tile);

            sendEntity(turret);
        });

        handler.register(CommandUnit.class, (con, data) -> {
            var object = Groups.sync.get(data.netId);
            if (object instanceof Unit unit) unit.target = data.target;
        });

        handler.register(ChatMessage.class, (con, data) -> {
            var player = Groups.players.find(p -> p.con == con);
            if (player != null && !data.message.isBlank() && data.message.length() <= maxMessageLength)
                Send.chatMessage(player.name, data.message);
        });
    }

    public void sendEntity(Entity entity) {
        Writes writes = Writes.of(sync.clear());

        writes.b(entity.typeId);
        entity.write(writes);

        Send.entity((short) sync.position(), sync.array());
    }

    public void sendSnapshot() {
        Writes writes = Writes.of(sync.clear());
        byte sent = 0;

        for (var object : Groups.sync) {
            sent++;

            writes.i(object.netId);
            object.writeSnapshot(writes);

            if (sync.position() > maxSnapshotSize || sent >= 255) {
                Send.snapshot((short) sync.position(), sync.array());

                sync.clear();
                sent = 0;
            }
        }

        if (sent > 0) Send.snapshot((short) sync.position(), sync.array());
    }

    public void sendWorldData(Connection connection) { // TODO replace connection by player
        try {
            // initialize new stream
            var output = new ByteArrayOutputStream();
            world.save(output);

            var input = new ByteArrayInputStream(output.toByteArray());
            Send.worldDataBegin(connection, input.available());

            // send the stream
            while (input.available() > 0) {
                byte[] bytes = new byte[Math.min(maxSnapshotSize, input.available())];
                input.read(bytes);

                Send.worldData(connection, (short) bytes.length, bytes); // short because the chunk size does not exceed maxSnapshotSize
            }
        } catch (IOException ex) {
            // TODO kick player
            return;
        }

        Groups.players.each(this::sendEntity); // TODO remove when Player inherits NetObject
        Send.state(connection, state);
    }

    // region listeners

    public void connected(Connection connection) {
        Groups.connections.add(connection);
        sendWorldData(connection);

        Log.info("@ received.", connection);
    }

    public void disconnected(Connection connection, DcReason reason) {
        Groups.connections.remove(connection);
        Groups.players.remove(player -> player.con == connection);
        // TODO mark player as disconnected and clear on game reset

        Log.info("@ disconnected: @.", connection, reason);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) try {
            handler.handle(connection, packet);
        } catch (Exception ignored) {
            Log.err("Unable to process client packet", ignored);
        }
    }

    public void idle(Connection connection) {}

    // endregion
}
