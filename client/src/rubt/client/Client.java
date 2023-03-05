package rubt.client;

import arc.func.Cons;
import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;
import arc.util.Threads;
import rubt.Groups;
import rubt.logic.State;
import rubt.net.*;
import rubt.net.Net.NetProvider;
import rubt.net.Packet.*;
import rubt.world.*;

import java.io.IOException;
import java.net.DatagramPacket;

import static rubt.Vars.*;

public class Client extends arc.net.Client implements NetListener, NetProvider {

    public PacketHandler handler = new PacketHandler();

    public Client() {
        super(8192, 8192, new PacketSerializer());
        addListener(this);

        handler.register(StateUpdate.class, update -> {
            state = State.values()[update.id];
        });

        handler.register(TileCreate.class, TileCreate::execute);
        handler.register(TileUpdate.class, update -> {});

        handler.register(UnitCreate.class, UnitCreate::execute);
        handler.register(UnitUpdate.class, update -> {
            Unit unit = Groups.units.get(update.unitID);
            unit.moveTo(update.position);
            unit.target = update.target;
            unit.rotation = update.rotation;
        });

        handler.register(TurretCreate.class, TurretCreate::execute);
        handler.register(TurretUpdate.class, update -> {
            Turret turret = Groups.turrets.get(update.turretID);
            turret.rotation = update.rotation;
        });
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