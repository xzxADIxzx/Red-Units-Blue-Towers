package rubt.net;

import arc.func.Prov;
import arc.math.geom.Position;
import arc.net.Connection;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import rubt.Groups;
import rubt.annotations.Annotations.Con;
import rubt.annotations.Annotations.Sendable;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.io.*;
import rubt.logic.*;
import rubt.types.TurretType;
import rubt.types.UnitType;
import rubt.world.*;

import java.io.*;

public class Packets {

    private static final Seq<Prov<? extends Packet>> provs = new Seq<>();
    private static final ObjectIntMap<Class<?>> packetToId = new ObjectIntMap<>();

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
        register(UpdateCursor::new);
        register(Snapshot::new);
        register(CreateEntity::new);
        register(SpawnUnit::new);
        register(BuildTurret::new);
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class DataPacket implements Packet {

        public short amount;
        public byte[] data;

        public void write(Writes w) {
            w.s(amount);
            w.b(data, 0, amount);
        }

        public void read(Reads r) {
            r.b(data = new byte[r.s()]);
        }
    }

    /** Packet used to confirm the connection by the client. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client)
    public static class PlayerData implements Packet {

        public byte[] avatar;
        public String name;

        public void write(Writes w) {
            w.nb(avatar);
            w.str(name);
        }

        public void read(Reads r) {
            avatar = r.nb(Image.rgbSize);
            name = r.str();
        }
    }

    /** Packet used to initialize the loading of the world. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable
    public static class WorldDataBegin implements Packet {

        public int amount;

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
    @Sendable
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable
    public static class UpdateState implements Packet {

        public State state;

        public void write(Writes w) {
            w.b(state.ordinal());
        }

        public void read(Reads r) {
            state = State.values()[r.b()];
        }
    }

    /** Packet used to update cursor position. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client, reliable = false)
    public static class UpdateCursor implements Packet {

        public Position cursor;

        public void write(Writes w) {
            w.p(cursor);
        }

        public void read(Reads r) {
            cursor = r.p();
        }
    }

    /** Packet used to update net objects. */
    @Sendable(connection = Con.server, reliable = false)
    public static class Snapshot extends DataPacket {

        public Snapshot() {}

        public Snapshot(short amount, byte[] data) {
            super(amount, data);
        }
    }

    /** Packet used to upload entities. */
    @Sendable(connection = Con.server)
    public static class CreateEntity extends DataPacket {

        public CreateEntity() {}

        public CreateEntity(short amount, byte[] data) {
            super(amount, data);
        }
    }

    /** Packet used to request a unit spawn. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client)
    public static class SpawnUnit implements Packet {

        public UnitType type;
        public Position position;

        public void write(Writes w) {
            w.b(type.id);
            w.p(position);
        }

        public void read(Reads r) {
            type = UnitTypes.all.get(r.b());
            position = r.p();
        }
    }

    /** Packet used to request a tower build. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client)
    public static class BuildTurret implements Packet {

        public TurretType type;
        public Tile tile;

        public void write(Writes w) {
            w.b(type.id);
            w.i(tile.id);
        }

        public void read(Reads r) {
            type = TurretTypes.all.get(r.b());
            tile = Groups.tiles.get(r.i());
        }
    }

    /** Packet used to command units. */
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client)
    public static class CommandUnit implements Packet {

        public int netId;
        public Position target;

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
    @NoArgsConstructor
    @AllArgsConstructor
    @Sendable(connection = Con.client)
    public static class ChatMessage implements Packet {

        public String message;

        public void write(Writes w) {
            w.str(message);
        }

        public void read(Reads r) {
            message = r.str();
        }
    }
}
