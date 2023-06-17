package rubt.content;

import arc.struct.Seq;
import arc.util.Log;
import rubt.types.ContentType;

@SuppressWarnings("unchecked")
public class ContentTypes {

    public static Seq<Seq<? extends ContentType<?>>> all;

    /** Create content types. */
    public static void load() {
        TileTypes.load();
        UnitTypes.load();
        TurretTypes.load();

        all = Seq.with(TileTypes.all, UnitTypes.all, TurretTypes.all);
        Log.info("[App] Loaded @ items of content.", all.sum(seq -> seq.size));
    }

    /** Load content textures. */
    public static void loadui() {
        TileTypes.loadui();
        UnitTypes.loadui();
        TurretTypes.loadui();
    }
}
