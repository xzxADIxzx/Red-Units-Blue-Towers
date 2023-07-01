package rubt.content;

import arc.struct.Seq;
import arc.util.Log;
import rubt.types.BulletType;
import rubt.types.ContentType;

@SuppressWarnings("unchecked")
public class ContentTypes {

    /** All types of content. */
    public static Seq<Seq<? extends ContentType<?>>> all;
    /** Bullet types are here because they are created with turrets and don't need a separate loader class. */
    public static Seq<BulletType> bullets = new Seq<>();

    /** Create content types. */
    public static void load() {
        TileTypes.load();
        UnitTypes.load();
        TurretTypes.load();

        all = Seq.with(TileTypes.all, UnitTypes.all, TurretTypes.all, bullets);
        Log.info("[App] Loaded @ items of content.", all.sum(seq -> seq.size));
    }

    /** Load content textures. */
    public static void loadui() {
        TileTypes.loadui();
        UnitTypes.loadui();
        TurretTypes.loadui();
    }
}
