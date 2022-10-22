package rubt.content;

import arc.struct.Seq;
import rubt.types.UnitType;

public class UnitTypes {

    public static Seq<UnitType> all = new Seq<>();

    /** Create unit types. */
    public static void load() {}

    /** Load unit's textures. */
    public static void loadui() {
        all.each(UnitType::loadui);
    }
}
