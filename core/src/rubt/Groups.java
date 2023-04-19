package rubt;

import arc.net.Connection;
import arc.struct.Seq;
import rubt.logic.Player;
import rubt.net.PacketSerializer.Reads;
import rubt.net.PacketSerializer.Writes;
import rubt.world.*;

public class Groups {

    public static Seq<NetObject> sync = new Seq<>();
    public static Seq<Connection> connections = new Seq<>();

    public static Seq<Tile> tiles = new Seq<>();
    public static Seq<Unit> units = new Seq<>();
    public static Seq<Turret> turrets = new Seq<>();
    public static Seq<Player> players = new Seq<>();

    public static void clear() {
        sync.clear();
        connections.clear();

        tiles.clear();
        units.clear();
        turrets.clear();
        players.clear();
    }

    @SuppressWarnings("unchecked")
    public static abstract class GroupObject {

        public final int id;

        public <T extends GroupObject> GroupObject(Seq<T> group) {
            this.id = group.size;
            group.add((T) this);
        }
    }

    public static abstract class NetObject extends GroupObject {

        public final int netId;

        public <T extends NetObject> NetObject(Seq<T> group) {
            super(group);

            this.netId = sync.size;
            sync.add(this);
        }

        public abstract void write(Writes w);

        public abstract void read(Reads w);
    }
}
