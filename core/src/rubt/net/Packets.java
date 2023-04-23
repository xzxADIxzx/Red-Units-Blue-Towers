package rubt.net;

import arc.func.Prov;
import arc.graphics.Pixmap;
import arc.math.geom.Position;
import arc.net.Connection;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import rubt.Groups;
import rubt.content.TurretTypes;
import rubt.content.UnitTypes;
import rubt.logic.*;
import rubt.net.PacketSerializer.*;
import rubt.types.TurretType;
import rubt.types.UnitType;
import rubt.world.*;

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
        register(Snapshot::new);
        register(UpdateState::new);
        register(PlayerData::new);
        register(CreatePlayer::new);
        register(CreateTile::new);
        register(CreateUnit::new);
        register(CreateTurret::new);
        register(CommandUnit::new);
        register(ChatMessage::new);
    }

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

    /** Packet used to update net objects. */
    public static class Snapshot implements Packet {

        public short amount;
        public byte[] data;

        public Snapshot() {}

        public Snapshot(short amount, byte[] data) {
            this.amount = amount;
            this.data = data;
        }

        public void write(Writes w) {
            w.writeShort(amount);
            w.write(data, 0, amount);
        }

        public void read(Reads r) {
            r.readFully(data = new byte[r.readShort()]);
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
            w.write(state.ordinal());
        }

        public void read(Reads r) {
            state = State.values()[r.readByte()];
        }
    }

    /** Player data packet that clients sends to confirm the connection. */
    public static class PlayerData implements Packet {

        public Pixmap avatar;
        public String name;

        public void write(Writes w) {
            w.writeStr(name);
        }

        public void read(Reads r) {
            name = r.readStr();
        }
    }

    /** Packet used to upload player datas. */
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
            w.write(team.ordinal());
            w.writeBoolean(admin);
        }

        public void read(Reads r) {
            super.read(r);
            team = Team.values()[r.readByte()];
            admin = r.readBoolean();
        }
    }

    /** Packet used to upload tiles. */
    public static class CreateTile implements Packet {

        public int pos;

        public CreateTile() {}

        public CreateTile(Tile tile) {
            this.pos = tile.pack();
        }

        public void write(Writes w) {
            w.writeInt(pos);
        }

        public void read(Reads r) {
            pos = r.readInt();
        }
    }

    /** Packet used to upload units. */
    public static class CreateUnit implements Packet {

        public UnitType type;
        public Position position;

        public CreateUnit() {}

        public CreateUnit(Unit unit) {
            this.type = unit.type;
            this.position = unit;
        }

        public void execute() {
            new Unit(type, position);
        }

        public void write(Writes w) {
            w.write(type.id);
            w.writePos(position);
        }

        public void read(Reads r) {
            type = UnitTypes.all.get(r.readByte());
            position = r.readPos();
        }
    }

    /** Packet used to upload turrets. */
    public static class CreateTurret implements Packet {

        public TurretType type;
        public Position position;

        public CreateTurret() {}

        public CreateTurret(Turret turret) {
            this.type = turret.type;
            this.position = turret;
        }

        public void execute() {
            new Turret(type, position);
        }

        public void write(Writes w) {
            w.write(type.id);
            w.writePos(position);
        }

        public void read(Reads r) {
            type = TurretTypes.all.get(r.readByte());
            position = r.readPos();
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
            w.writeInt(netId);
            w.writePos(target);
        }

        public void read(Reads r) {
            netId = r.readInt();
            target = r.readPos();
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
            w.writeStr(message);
        }

        public void read(Reads r) {
            message = r.readStr();
        }
    }
}
