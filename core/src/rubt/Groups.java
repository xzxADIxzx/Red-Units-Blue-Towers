package rubt;

import arc.net.Connection;
import arc.struct.Seq;
import rubt.world.*;

public class Groups {

    public static Seq<Tile> tiles = new Seq<>();
    public static Seq<Unit> units = new Seq<>();
    public static Seq<Turret> turrets = new Seq<>();
    public static Seq<Connection> connections = new Seq<>();

    public static void clear() {
        tiles.clear();
        units.clear();
        turrets.clear();
        connections.clear();
    }

    @SuppressWarnings("unchecked")
    public static abstract class GroupObject {

        public final int id;

        public <T extends GroupObject> GroupObject(Seq<T> group) {
            this.id = group.size;
            group.add((T)this);
        }
    }
}
