package rubt.net;

import arc.func.Prov;
import arc.graphics.Pixmap;
import arc.math.geom.Position;
import arc.net.Connection;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
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
        register(StateUpdate::new);
        register(TileCreate::new);
        register(UnitCreate::new);
        register(TurretCreate::new);
        register(PlayerCreate::new);
    }

    public static abstract class Packet {

        public void sendTCP(Connection connection) {
            connection.sendTCP(this);
        }

        public void sendUDP(Connection connection) {
            connection.sendUDP(this);
        }

        public void sendTCP(Player player) {
            player.con.sendTCP(this);
        }

        public void sendUDP(Player player) {
            player.con.sendUDP(this);
        }

        public abstract void write(Writes w);

        public abstract void read(Reads r);
    }

    /** Packet used to update net objects on clients. */
    public static class Snapshot extends Packet {

        public byte amount;
        public byte[] data;

        public Snapshot() {}

        public Snapshot(byte amount, byte[] data) {
            this.amount = amount;
            this.data = data;
        }

        public void write(Writes w) {
            w.write(amount);
            w.writeShort(data.length);
            w.write(data);
        }

        public void read(Reads r) {
            amount = r.readByte();
            r.readFully(data = new byte[r.readShort()]);
        }
    }

    /** Packet used to update game state on clients. */
    public static class StateUpdate extends Packet {

        public State state;

        public StateUpdate() {}

        public StateUpdate(State state) {
            this.state = state;
        }

        public void write(Writes w) {
            w.write(state.ordinal());
        }

        public void read(Reads r) {
            state = State.values()[r.readByte()];
        }
    }

    /** Player data packet used to sync player datas on clients. Clients sends this packet after connecting. */
    public static class PlayerCreate extends Packet {

        public Pixmap avatar;
        public String name;
        public Team team;
        public boolean admin;

        public PlayerCreate() {}

        public PlayerCreate(Player player) {
            this.avatar = player.avatar;
            this.name = player.name;
            this.team = player.team;
            this.admin = player.admin;
        }

        public void write(Writes w) {
            w.writeStr(name);
            w.write(team.ordinal());
            w.writeBoolean(admin);
        }

        public void read(Reads r) {
            avatar = null;
            name = r.readStr();
            team = Team.values()[r.readByte()];
            admin = r.readBoolean();
        }
    }

    /** Tile data packet used to upload tile to clients. */
    public static class TileCreate extends Packet {

        public int pos;

        public TileCreate() {}

        public TileCreate(Tile tile) {
            this.pos = tile.pack();
        }

        public void write(Writes w) {
            w.writeInt(pos);
        }

        public void read(Reads r) {
            pos = r.readInt();
        }
    }

    /** Unit data packet used to create new unit on clients. */
    public static class UnitCreate extends Packet {

        public UnitType type;
        public Position position;

        public UnitCreate() {}

        public UnitCreate(Unit unit) {
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

    /** Unit data packet used to create new unit on clients. */
    public static class TurretCreate extends Packet {

        public TurretType type;
        public Position position;

        public TurretCreate() {}

        public TurretCreate(Turret turret) {
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
}
