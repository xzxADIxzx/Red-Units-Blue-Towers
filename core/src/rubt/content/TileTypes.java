package rubt.content;

import arc.struct.Seq;
import rubt.types.TileType;
import rubt.types.tiles.EmptyTile;

public class TileTypes {

    public static Seq<TileType> all = new Seq<>();

    public static EmptyTile air;

    /** Create tile types. */
    public static void load() {
        air = new EmptyTile("air") {{
            solid = true;
        }};
    }

    /** Load tiles textures. */
    public static void loadui() {
        all.each(TileType::loadui);
    }
}
