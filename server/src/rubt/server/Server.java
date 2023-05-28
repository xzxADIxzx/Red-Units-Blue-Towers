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
import rubt.world.Unit;

import static rubt.Vars.*;

import java.io.*;
import java.nio.ByteBuffer;

public class Server extends arc.net.Server implements NetListener {

    public PacketHandler handler = new PacketHandler();

    /** Output for writing snapshots. */
    public Writes sync = new Writes(ByteBuffer.allocate(1024));

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

        handler.register(PlayerData.class, (con, data) -> {
            if (rules.strict && Groups.players.contains(player -> con.getRemoteAddressTCP().getAddress().getHostName().equals(player.ip()))) return;

            Player player = new Player(con);

            player.avatar = data.avatar;
            player.name = data.name;
            player.team = Logic.nextTeam();
            player.admin = false; // TODO admin system?

            Send.createPlayer(player);
        });

        handler.register(CreateUnit.class, (con, data) -> {
            data.execute();
            Send.createUnit(Groups.units.peek());
        });

        handler.register(CreateTurret.class, (con, data) -> {
            data.execute();
            Send.createTurret(Groups.turrets.peek());
        });

        handler.register(CommandUnit.class, (con, data) -> {
            var object = Groups.sync.get(data.netId);
            if (object instanceof Unit unit) unit.target = data.target;
        });

        handler.register(ChatMessage.class, (con, data) -> {
            var player = Groups.players.find(p -> p.con == con);
            if (player != null && !data.message.isBlank() && data.message.length() <= maxMessageLength) Send.chatMessage(player, data.message);
        });
    }

    public void sendSnapshot() {
        sync.buffer.clear();
        byte sent = 0;

        for (var object : Groups.sync) {
            sent++;

            sync.writeInt(object.netId);
            object.write(sync);

            if (sync.buffer.position() > maxSnapshotSize || sent >= 255) {
                Send.snapshot((short) sync.buffer.position(), sync.buffer.array());

                sync.buffer.clear();
                sent = 0;
            }
        }

        if (sent > 0) Send.snapshot((short) sync.buffer.position(), sync.buffer.array());
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
        Groups.players.remove(player -> player.con == connection);
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
