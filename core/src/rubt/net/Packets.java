package rubt.net;

import arc.func.Prov;
import arc.graphics.Pixmap;
import arc.math.geom.Position;
import arc.net.Connection;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import rubt.Groups;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.logic.*;
import rubt.world.*;

import java.io.*;

public class Packets {

    private static Seq<Prov<? extends Packet>> provs = new Seq<>();
    private static ObjectIntMap<Class<?>> packetToId = new ObjectIntMap<>();

    /** Registers a new packet type for serialization. */
    public static void register(Prov<? extends Packet> prov) {
        provs.add(prov);
        packetToId.put(prov.get().getClass(), provs.size - 1);
    }

    /** Returns the packet id used for serialization. */
    public static byte packetId(Packet packet) {
        int id = packetToId.get(packet.getClass(), -1);
        if (id == -1) throw new RuntimeException("Unknown packet!"); // didn't you forget to register the package?

        return (byte) id;
    }

    /** Creates a new package by the given id. */
    public static Packet newPacket(byte id) {
        return provs.get(id).get();
    }

    public static void load() {
        register(PlayerData::new);
        register(WorldDataBegin::new);
        register(WorldData::new);
        register(UpdateState::new);
        register(Snapshot::new);
        register(CreatePlayer::new);
        register(CommandUnit::new);
        register(ChatMessage::new);
    }

    /** Packet containing any data to be sent over the network. */
    public static interface Packet {

        default void sendTCP(Connection connection) {
            connection.sendTCP(this);
        }

        default void sendUDP(Connection connection) {
            connection.sendUDP(this);
        }

        default void sendTCP(Player player) {
            player.con.sendTCP(this);
        }

        default void sendUDP(Player player) {
            player.con.sendUDP(this);
        }

        default void sendTCP() {
            Groups.players.each(this::sendTCP);
        }

        default void sendUDP() {
            Groups.players.each(this::sendUDP);
        }

        void write(Writes w);

        void read(Reads r);
    }

    /** Packet containing data in the form of bytes. Usually it is world or entity data. */
    public static abstract class DataPacket implements Packet {

        public short amount;
        public byte[] data;

        public DataPacket() {}

        public DataPacket(short amount, byte[] data) {
            this.amount = amount;
            this.data = data;
        }

        public void write(Writes w) {
            w.s(amount);
            w.b(data, 0, amount);
        }

        public void read(Reads r) {
            r.b(data = new byte[r.s()]);
        }
    }

    /** Packet used to confirm the connection by the client. */
    public static class PlayerData implements Packet {

        public Pixmap avatar;
        public String name;

        public void write(Writes w) {
            w.str(name);
        }

        public void read(Reads r) {
            name = r.str();
        }
    }

    /** Packet used to initialize the loading of the world. */
    public static class WorldDataBegin implements Packet {

        public int amount;

        public WorldDataBegin() {}

        public WorldDataBegin(int amount) {
            this.amount = amount;
        }

        public void write(Writes w) {
            w.i(amount);
        }

        public void read(Reads r) {
            amount = r.i();
        }

        public WorldDataBuilder builder() {
            return new WorldDataBuilder(amount);
        }
    }

    /** Packet containing one world data chunk. */
    public static class WorldData extends DataPacket {

        public WorldData() {}

        public WorldData(short amount, byte[] data) {
            super(amount, data);
        }
    }

    /** Builder for reading world data. It's not a packet, but for convenience I have put it here. */
    public static class WorldDataBuilder {

        public final int amount;
        public final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        public WorldDataBuilder(int amount) {
            this.amount = amount;
        }

        public void add(byte[] bytes) throws IOException {
            stream.write(bytes);
        }

        public float progress() {
            return (float) stream.size() / amount;
        }

        public ByteArrayInputStream build() {
            return new ByteArrayInputStream(stream.toByteArray());
        }
    }

    /** Packet used to update game state. */
    public static class UpdateState implements Packet {

        public State state;

        public UpdateState() {}

        public UpdateState(State state) {
            this.state = state;
        }

        public void write(Writes w) {
            w.b(state.ordinal());
        }

        public void read(Reads r) {
            state = State.values()[r.b()];
        }
    }

    /** Packet used to update net objects. */
    public static class Snapshot extends DataPacket {

        public Snapshot() {}

        public Snapshot(short amount, byte[] data) {
            super(amount, data);
        }
    }

    /** Packet used to upload player data. */
    public static class CreatePlayer extends PlayerData {

        public Team team;
        public boolean admin;

        public CreatePlayer() {}

        public CreatePlayer(Player player) {
            this.avatar = player.avatar;
            this.name = player.name;
            this.team = player.team;
            this.admin = player.admin;
        }

        public void write(Writes w) {
            super.write(w);
            w.b(team.ordinal());
            w.bool(admin);
        }

        public void read(Reads r) {
            super.read(r);
            team = Team.values()[r.b()];
            admin = r.bool();
        }
    }

    /** Packet used to command units. */
    public static class CommandUnit implements Packet {

        public int netId;
        public Position target;

        public CommandUnit() {}

        public CommandUnit(Unit unit, Position target) {
            this.netId = unit.netId;
            this.target = target;
        }

        public void write(Writes w) {
            w.i(netId);
            w.p(target);
        }

        public void read(Reads r) {
            netId = r.i();
            target = r.p();
        }
    }

    /** Package used for communication between players. */
    public static class ChatMessage implements Packet {

        public String message;

        public ChatMessage() {}

        public ChatMessage(String message) {
            this.message = message;
        }

        public void write(Writes w) {
            w.str(message);
        }

        public void read(Reads r) {
            message = r.str();
        }
    }
}
