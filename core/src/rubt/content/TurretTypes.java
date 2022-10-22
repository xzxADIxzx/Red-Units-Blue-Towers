package rubt.content;

import arc.struct.Seq;
import rubt.types.TurretType;

public class TurretTypes {

    public static Seq<TurretType> all = new Seq<>();

    /** Create turret types. */
    public static void load() {}

    /** Load turret's textures. */
    public static void loadui() {
        all.each(TurretType::loadui);
    }
}
