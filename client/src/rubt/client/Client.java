package rubt.client;

import arc.func.Cons;
import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import arc.util.Threads;
import rubt.Groups;
import rubt.logic.Player;
import rubt.net.*;
import rubt.net.Net.NetProvider;
import rubt.net.PacketSerializer.Reads;
import rubt.net.Packets.*;
import rubt.world.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import static rubt.Vars.*;

public class Client extends arc.net.Client implements NetListener, NetProvider {

    public PacketHandler handler = new PacketHandler();

    /** Input for reading snapshots. */
    public Reads sync = new Reads(null);

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);

        handler.register(Snapshot.class, this::readSnapshot);
        handler.register(StateUpdate.class, update -> state = update.state);

        handler.register(PlayerCreate.class, create -> {
            Player player = new Player(null);

            player.avatar = create.avatar;
            player.name = create.name;
            player.team = create.team;
            player.admin = create.admin;

            ui.lobbyfrag.rebuildList();
        });

        handler.register(TileCreate.class, create -> new Tile(create.pos));
        handler.register(UnitCreate.class, UnitCreate::execute);
        handler.register(TurretCreate.class, TurretCreate::execute);
    }

    public void readSnapshot(Snapshot snapshot) {
        try {
            sync.setBuffer(ByteBuffer.wrap(snapshot.data));

            while (sync.buffer.hasRemaining()) {
                var object = Groups.sync.get(sync.readInt());
                object.read(sync);
            }
        } catch (Exception ignored) {
            Log.err("Error reading snapshot", ignored);
        }
    }

    // region provider

    public void connect(String ip, int port) throws IOException {
        thread = Threads.daemon("Client", this::run);
        connect(5000, ip, port, port);
    }

    public void disconnect() {
        thread = null;
        close();
    }

    public void discover(Cons<DatagramPacket> cons, Runnable done) {
        discoverHosts(port, multicast, multicastPort, 5000, cons, done);
    }

    // endregion
    // region listeners

    public void connected(Connection connection) {
        var tcp = connection.getRemoteAddressTCP();
        Log.info("Connected to @:@", tcp.getHostName(), tcp.getPort());

        Send.player();
    }

    public void disconnected(Connection connection, DcReason reason) {
        Log.info("Connection closed: @.", reason);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) try {
            handler.handle(connection, packet);
        } catch (Throwable ignored) {
            Log.err("Unable to process server packet", ignored);
        }
    }

    public void idle(Connection connection) {}

    // endregion
}