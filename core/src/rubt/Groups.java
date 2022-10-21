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
}
