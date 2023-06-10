package rubt.client;

import arc.func.Cons;
import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import arc.util.Threads;
import rubt.Groups;
import rubt.io.Reads;
import rubt.logic.Player;
import rubt.logic.State;
import rubt.net.*;
import rubt.net.Net.NetProvider;
import rubt.net.Packets.*;
import rubt.world.Entities;

import static arc.Core.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;

import static rubt.Vars.*;

public class Client extends arc.net.Client implements NetListener, NetProvider {

    public PacketHandler handler = new PacketHandler();

    /** Builder for reading world data. */
    public WorldDataBuilder builder;

    /** Executor for fetching server info. */
    public final ExecutorService fetchExecutor = Threads.unboundedExecutor("Fetch Servers");

    /** Statistics for debug fragment. */
    public int packetsReaded, packetsWritten;
    public long bytesReaded, bytesWritten; // TODO per second (how?)

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);

        handler.register(WorldDataBegin.class, data -> {
            builder = data.builder();
            ui.loadfrag.show(builder::progress);
        });

        handler.register(WorldData.class, this::readWorldData);

        handler.register(UpdateState.class, data -> {
            state = data.state;
            ui.chatfrag.alpha = 1f;
        });

        handler.register(Snapshot.class, this::readSnapshot);

        handler.register(CreatePlayer.class, data -> {
            Player player = new Player(null);

            player.avatar = data.avatar;
            player.name = data.name;
            player.team = data.team;
            player.admin = data.admin;

            ui.lobbyfrag.rebuildList();
        });

        handler.register(CreateEntity.class, data -> {
            ByteBuffer buffer = ByteBuffer.wrap(data.data);
            Reads reads = Reads.of(buffer);

            var entity = Entities.newEntity(reads.b());
            entity.read(reads);
        });

        handler.register(ChatMessage.class, data -> ui.chatfrag.flush(data.message));
    }

    public void readSnapshot(Snapshot snapshot) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(snapshot.data);
            Reads reads = Reads.of(buffer);

            while (buffer.hasRemaining()) {
                var object = Groups.sync.get(reads.i());
                object.readSnapshot(reads);
            }
        } catch (Exception ignored) {
            Log.err("Error reading snapshot", ignored);
        }
    }

    public void readWorldData(WorldData data) {
        try {
            builder.add(data.data);

            if (builder.progress() != 1f) return;

            world.load(builder.build());
            builder = null;

            ui.loadfrag.hide();
        } catch (IOException ex) {
            // TODO disconnect
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

    public void fetchServerInfo(String ip, int port, Cons<DatagramPacket> cons) {
        fetchExecutor.submit(() -> {
            try (var socket = new DatagramSocket()) {
                socket.send(new DatagramPacket(new byte[] { 1, 4 }, 2, InetAddress.getByName(ip), port));
                socket.setSoTimeout(2000);

                var packet = new DatagramPacket(new byte[256], 256);
                socket.receive(packet);

                cons.get(packet);
            } catch (Exception ignored) {
                Log.debug("Unable to fetch server datagram packet.");
            }
        });
    }

    public void readed(long bytes) {
        packetsReaded++;
        bytesReaded += bytes;
    }

    public void written(long bytes) {
        packetsWritten++;
        bytesWritten += bytes;
    }

    public int packetsReaded() {
        return packetsReaded;
    }

    public int packetsWritten() {
        return packetsWritten;
    }

    public long bytesReaded() {
        return bytesReaded;
    }

    public long bytesWritten() {
        return bytesWritten;
    }

    // endregion
    // region listeners

    public void connected(Connection connection) {
        var tcp = connection.getRemoteAddressTCP();
        Log.info("Connected to @:@", tcp.getHostName(), tcp.getPort());

        Send.playerData(settings.getBytes("player-avatar"), settings.getString("player-name"));
    }

    public void disconnected(Connection connection, DcReason reason) {
        Groups.clear();
        state = State.menu;

        Log.info("Connection closed: @.", reason);
    }

    public void received(Connection connection, Object object) {
        if (object instanceof Packet packet) try {
            handler.handle(connection, packet);
        } catch (Exception ignored) {
            Log.err("Unable to process server packet", ignored);
        }
    }

    public void idle(Connection connection) {}

    // endregion
}