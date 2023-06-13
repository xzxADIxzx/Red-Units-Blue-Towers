package rubt.content;

import arc.struct.Seq;
import rubt.types.TileType;

public class TileTypes {

    public static Seq<TileType> all = new Seq<>();

    /** Create tile types. */
    public static void load() {}

    /** Load tiles textures. */
    public static void loadui() {
        all.each(TileType::loadui);
    }
}
