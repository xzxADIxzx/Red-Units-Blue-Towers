package rubt;

import arc.math.geom.Position;
import arc.net.Connection;
import arc.struct.Seq;
import rubt.io.Reads;
import rubt.io.Writes;
import rubt.logic.Player;
import rubt.world.*;

@SuppressWarnings("unchecked")
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

    /** Any object belonging to any group. Usually it's a content type or entity. */
    public static abstract class GroupObject {

        public final int id;

        public <T extends GroupObject> GroupObject(Seq<T> group) {
            this.id = group.size;
            group.add((T) this);
        }
    }

    /** Layer between {@link GroupObject} and {@link NetObject}. Needed for tiles. */
    public static abstract class Entity extends GroupObject implements Position {

        public final int typeId;

        public <T extends Entity> Entity(Seq<T> group) {
            super(group);
            this.typeId = Entities.typeId(this);
        }

        public abstract void write(Writes w);

        public abstract void read(Reads r);
    }

    /** Any entity that has updatable state synchronized across the network. */
    public static abstract class NetObject extends Entity {

        public final int netId;

        public <T extends NetObject> NetObject(Seq<T> group) {
            super(group);

            this.netId = sync.size;
            sync.add(this);
        }

        public abstract void writeSnapshot(Writes w);

        public abstract void readSnapshot(Reads r);

        public abstract void interpolate();
    }
}
